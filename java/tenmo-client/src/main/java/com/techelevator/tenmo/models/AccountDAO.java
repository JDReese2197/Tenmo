package com.techelevator.tenmo.models;

import java.sql.JDBCType;

public interface AccountDAO {
	
	// need account id
	// Account balance
	
	public double getAccountBalance(double balance);
	
	public Account getAccountById(double id);
	
	public Account getAccountId(double id);
	
	public double addBalance();
		
	public double subtractBalance();
	
	public boolean checkValidTransfer();
	

}
