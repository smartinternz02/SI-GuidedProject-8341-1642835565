package com.viruchith.springexpensetracker.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viruchith.springexpensetracker.models.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

}
