package com.techelevator.tenmo.model;

import java.sql.JDBCType;

public interface AccountDAO {
	
	// need account id
	// Account balance
	
	public double getAccountBalance(int id);
	
	public Account getAccountById(int id);
	
	public Account getAccountId(int id);
	
	public double addBalance();
		
	public double subtractBalance();
	
	public boolean checkValidTransfer();
	

}
