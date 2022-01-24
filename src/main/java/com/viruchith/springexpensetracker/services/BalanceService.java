package com.viruchith.springexpensetracker.services;

import com.viruchith.springexpensetracker.models.Balance;

public interface BalanceService {
	public Balance saveBalance(Balance balance);
	public Balance createBalance();
}
