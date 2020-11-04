package com.techelevator.tenmo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.JDBCTransferDAO;
import com.techelevator.tenmo.model.Transfer;

public class TransferController {

	
	private JDBCTransferDAO transferDAO;
    
    
    // Constructor
    public TransferController(JDBCTransferDAO transferDAO) {
       this.transferDAO = transferDAO;
    }
    
    @RequestMapping(value = "transfers", method = RequestMethod.GET)
    public List<Transfer> listAllTransfers() {
    	List<Transfer> transfer = transferDAO.getAllTransfers();
    	logAPICall("GET - transfers");
    	return transfer;
    }
    
    @RequestMapping(value = "transfers/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(int id) {
    	Transfer transfer = transferDAO.getTransferById(id);
    	logAPICall("GET - transfers by id: " + id);
    	return transfer;
    }
    
    public void logAPICall(String message) {
    	LocalDateTime now = LocalDateTime.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss.A");
	    String timeNow = now.format(formatter);
	    System.out.println(timeNow + ": " + message);
    }
    
}
