package com.techelevator.tenmo.models;






public class JDBCAccountDAO  implements AccountDAO {

	private JdbcTemplate jdbcTemplate;
	
	public JDBCAccountDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	
	/*********************************************
	 * Interface methods
	 *********************************************/


	@Override
	public List<Account> getAllAccounts() {
		List<Account> accounts = new ArrayList<Account>();
		
		return accounts;
	}
	
	@Override
	public double getAccountBalance(double balance) {
		return 0;
	}


	@Override
	public Account getAccountById(double id) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Account getAccountId(double id) {
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
		
	}
	
	

}
