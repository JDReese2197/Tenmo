package com.techelevator.tenmo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.JDBCTransferDAO;
import com.techelevator.tenmo.model.Transfer;

@RestController
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
    
    @RequestMapping(value = "transfers/accounts/{id}", method = RequestMethod.GET)
    public List<Transfer> getAllTransfersByAccountId(@PathVariable int id) {
    	List<Transfer> transfers = transferDAO.getAllTransfersByAccountId(id);
    	return transfers;
    }
    
    @RequestMapping(value = "transfers/accountFrom/{id}")
    public List<Transfer> getTransfersByAccountFromId(@PathVariable int id) {
    	List<Transfer> transfers = transferDAO.getTransfersByFromId(id);
    	return transfers;
    }
    
    @RequestMapping(value = "transfers/accountTo/{id}")
    public List<Transfer> getTransfersByAccountToId(@PathVariable int id) {
    	List<Transfer> transfers = transferDAO.getTransfersByToId(id);
    	return transfers;
    }
    public void logAPICall(String message) {
    	LocalDateTime now = LocalDateTime.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss.A");
	    String timeNow = now.format(formatter);
	    System.out.println(timeNow + ": " + message);
    }
    
}
