package com.techelevator.tenmo.models;

import java.util.List;

public interface TransferDAO {
	
	public List<Transfer> getAllTransfers();
	public List<Transfer> getTransferById(int id);
	

}
