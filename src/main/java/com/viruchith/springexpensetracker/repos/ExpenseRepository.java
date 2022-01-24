package com.viruchith.springexpensetracker.repos;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viruchith.springexpensetracker.models.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	public List<Expense> findByCreatedAtBetween(Date startCreatedAt,Date endCreatedAt);
}
