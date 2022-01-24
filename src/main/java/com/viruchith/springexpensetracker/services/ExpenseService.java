package com.viruchith.springexpensetracker.services;

import java.util.Date;
import java.util.List;

import com.viruchith.springexpensetracker.models.Expense;

public interface ExpenseService {
	public Expense saveExpense(Expense expense);
	public List<Expense> filterExpenseBetween(Date startDate,Date endDate);
}
