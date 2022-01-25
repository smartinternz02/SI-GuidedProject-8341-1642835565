package com.viruchith.springexpensetracker.serviceimpls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viruchith.springexpensetracker.models.Expense;
import com.viruchith.springexpensetracker.models.User;
import com.viruchith.springexpensetracker.repos.ExpenseRepository;
import com.viruchith.springexpensetracker.services.ExpenseService;

@Service
public class ExpenseServiceImplementation implements ExpenseService {

	@Autowired
	private ExpenseRepository expenseRepository;
	
	@Override
	public Expense saveExpense(Expense expense) {
		// TODO Auto-generated method stub
		return expenseRepository.save(expense);
	}

	@Override
	public List<Expense> filterExpenseBetween(String startDate, String endDate) {
		// TODO Auto-generated method stub
		return this.expenseRepository.findByCreatedAtBetween(startDate, endDate);
	}

	@Override
	public List<Expense> filterUserExpenseBetween(User user,String startDate, String endDate) {
		// TODO Auto-generated method stub
		return this.expenseRepository.findByUserAndCreatedAtBetween(user, startDate, endDate);
	}

	
}
