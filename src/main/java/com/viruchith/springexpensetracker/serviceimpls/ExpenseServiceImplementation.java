package com.viruchith.springexpensetracker.serviceimpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viruchith.springexpensetracker.models.Expense;
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

}
