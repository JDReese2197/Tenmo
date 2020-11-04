package com.techelevator.tenmo.model;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JDBCAccountDAO  implements AccountDAO {

	private JdbcTemplate jdbcTemplate;
	
	public JDBCAccountDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	
	/*********************************************
	 * Interface methods
	 *********************************************/


	public List<Account> getAllAccounts() {
		List<Account> accounts = new ArrayList<Account>();
		String query = "SELECT account_id, balance, user_id FROM accounts";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(query);
		while(results.next()) {
			Account accountResult = mapRowToAccount(results);
			accounts.add(accountResult);
		}
		return accounts;
	}
	
	@Override
	public double getAccountBalance(int id) {
		Account account = getAccountById(id);
		return account.getBalance();
	}


	@Override
	public Account getAccountById(int id) {
		String query = "SELECT * FROM account WHERE account_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(query, id);
		if(results.next()) {
			return mapRowToAccount(results);
		}
		System.out.println("Account not found");
		return null;
	}


	@Override
	public Account getAccountId(int id) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public double addBalance() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public double subtractBalance() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public boolean checkValidTransfer() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private Account mapRowToAccount(SqlRowSet result) {
		Account account = new Account();
		account.setAccountId(result.getInt("account_id"));
		account.setBalance(result.getDouble("balance"));
		account.setUserId(result.getInt("user_id"));
		return account;
	}
	
	

}
