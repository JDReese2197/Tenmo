package com.techelevator.tenmo.model;

import java.util.List;

public interface TransferDAO {
	
	public List<Transfer> getAllTransfers();
	public Transfer getTransferById(int id);
	

}
