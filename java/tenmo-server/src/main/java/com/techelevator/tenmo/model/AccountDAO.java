package com.techelevator.tenmo.model;

import java.sql.JDBCType;
import java.util.List;

public interface AccountDAO {
	
	// need account id
	// Account balance
	
	public double getAccountBalance(int id);
	
	public Account getAccountById(int id);
	
	public double addBalance(int id, double amount);
		
	public double subtractBalance(int id, double amount);

	public Account getAccountByUserId(long id);
	
	public boolean checkValidTransfer();

	public List<Account> getAllAccounts();
}
