package com.viruchith.springexpensetracker.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.viruchith.springexpensetracker.jwt.AppUserDetailsService;
import com.viruchith.springexpensetracker.jwt.IncorrectUsernameOrPasswordException;
import com.viruchith.springexpensetracker.jwt.JwtRequest;
import com.viruchith.springexpensetracker.jwt.JwtResponse;
import com.viruchith.springexpensetracker.jwt.JwtUtil;
import com.viruchith.springexpensetracker.models.Balance;
import com.viruchith.springexpensetracker.models.Expense;
import com.viruchith.springexpensetracker.models.ExpenseResponse;
import com.viruchith.springexpensetracker.models.User;
import com.viruchith.springexpensetracker.services.BalanceService;
import com.viruchith.springexpensetracker.services.ExpenseService;
import com.viruchith.springexpensetracker.services.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private AppUserDetailsService appUserDetailsService;
	
	@Autowired
	private BalanceService balanceService;
	
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ExpenseService expenseService;
	
	@GetMapping("")
	public String index(){
		SecurityContext sc =  SecurityContextHolder.getContext();
		Authentication authentication = sc.getAuthentication();
		
		return "Hello "+authentication.getName()+" From APP !!";
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody JwtRequest jwtRequest) throws IncorrectUsernameOrPasswordException{
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
		} catch (BadCredentialsException e) {
			// TODO: handle exception
			throw new IncorrectUsernameOrPasswordException();
		}
		
		UserDetails userDetails = appUserDetailsService.loadUserByUsername(jwtRequest.getUsername());
		
		String token = jwtUtil.generateToken(userDetails);
		MultiValueMap<String, String> headers = new HttpHeaders();
		headers.add("auth-token", token);
		ResponseEntity<JwtResponse> responseEntity = new ResponseEntity<JwtResponse>(new JwtResponse(token), headers, HttpStatus.OK);
		return responseEntity;
		
	}
	
	@PostMapping("/expense")
	public ResponseEntity<?> saveExpense(@RequestBody @Valid Expense expense){
		User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		expense.setUser(user);
		expense.setCreatedAt(dateFormat.format(date));
		expense = expenseService.saveExpense(expense);
		
		user.addExpenses(expense);
		
		Balance balance = user.getBalance();
		
		if(balance.getAmount()>0) {
			double deductedAmount = balance.getAmount()-expense.getAmount();
			balance.setAmount((deductedAmount>0)?deductedAmount:0.0);
		}
		
		userService.saveUser(user);
		
		ExpenseResponse expenseResponse = new ExpenseResponse();
		
		expenseResponse.setId(expense.getId());
		expenseResponse.setTitle(expense.getTitle());
		expenseResponse.setNote(expense.getNote());
		expenseResponse.setAmount(expense.getAmount());
		expenseResponse.setCategory(expense.getCategory());
		
		return ResponseEntity.ok(expenseResponse);
	}
	
	@GetMapping("/expense/all")
	public ResponseEntity<List<Expense>> getAllUserExpenses(){
		User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		return ResponseEntity.ok(user.getExpensesList());
	}
	
	@GetMapping("/expense/filter")
	public ResponseEntity<List<ExpenseResponse>> filterUserExpenses(@RequestParam(name = "startDate") String startDate,@RequestParam(name = "endDate") String endDate) throws ParseException{
		User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		List<Expense> expenses = expenseService.filterUserExpenseBetween(user, startDate, endDate);				
		List<ExpenseResponse> expenseResponses = expenses.stream().map(e->{
			ExpenseResponse expenseResponse = new ExpenseResponse();
			expenseResponse.setId(e.getId());
			expenseResponse.setAmount(e.getAmount());
			expenseResponse.setCategory(e.getCategory());
			expenseResponse.setCreatedAt(e.getCreatedAt());
			expenseResponse.setNote(e.getNote());
			expenseResponse.setTitle(e.getTitle());
			return expenseResponse;
		}).collect(Collectors.toList());
		return ResponseEntity.ok(expenseResponses);
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> signUpUser(@RequestBody @Valid User user){
		Balance balance  = new Balance(0, new Date());
		user.setBalance(balanceService.saveBalance(balance));
		user.setCreatedAt(new Date());
		User savedUser = this.userService.createUser(user);
		user.setId(savedUser.getId());
		user.setCreatedAt(savedUser.getCreatedAt());
		user.setPassword(null);
		return ResponseEntity.ok(user);
	}
	
	@GetMapping("/balance")
	public ResponseEntity<Balance> getBalance(@Valid @RequestBody Balance balance){
		User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		return ResponseEntity.ok(user.getBalance());
	}
	
	@PatchMapping("/balance")
	public ResponseEntity<Balance> updateBalance(@Valid @RequestBody Balance balance){
		User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		user.getBalance().addAmount((balance.getAmount()));
		userService.saveUser(user);
		return ResponseEntity.ok(user.getBalance());
	}
	
	
}
