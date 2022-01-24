package com.viruchith.springexpensetracker.controllers;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viruchith.springexpensetracker.models.Balance;
import com.viruchith.springexpensetracker.models.Expense;
import com.viruchith.springexpensetracker.models.User;
import com.viruchith.springexpensetracker.services.ExpenseService;
import com.viruchith.springexpensetracker.services.UserService;

@RestController
@RequestMapping("/expense")
@CrossOrigin
public class ExpenseController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ExpenseService expenseService;
	
	@PostMapping("")
	public ResponseEntity<?> saveExpense(@RequestBody @Valid Expense expense){
		
		expense.setCreatedAt(new Date());
		expense = expenseService.saveExpense(expense);
		
		User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		user.addExpenses(expense);
		
		Balance balance = user.getBalance();
		
		if(balance.getAmount()>0) {
			double deductedAmount = balance.getAmount()-expense.getAmount();
			balance.setAmount((deductedAmount>0)?deductedAmount:0.0);
		}
		
		userService.saveUser(user);
		
		return ResponseEntity.ok(expense);
	}
	
}
