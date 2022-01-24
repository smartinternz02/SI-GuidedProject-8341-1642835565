package com.viruchith.springexpensetracker.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viruchith.springexpensetracker.models.Balance;

public interface BalanceRepository extends JpaRepository<Balance, Long> {

}
