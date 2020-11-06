package com.techelevator.tenmo;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.BrokeUserException;
import com.techelevator.view.ConsoleService;

import java.util.Arrays;
import java.util.List;

import javax.naming.NameNotFoundException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;



public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private Account account;
	private RestTemplate apiCall = new RestTemplate();
    
    

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService) {
		this.console = console;
		this.authenticationService = authenticationService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}
	
	/*********************************************
	 * 
	 * As an authenticated user of the system, 
	 * I need to be able to see my Account Balance.
	 * 
	 *********************************************/

	private void viewCurrentBalance() {
		User user = currentUser.getUser();
		System.out.println("View Current Balance");
		
		Account accounts = apiCall.getForObject(API_BASE_URL + "accounts/" + user.getId(), Account.class);
		// ResponseEntity<Account> responseEntity = apiCall.getForEntity(API_BASE_URL + "accounts", Account.class);
		// double currentBalance =  (responseEntity.getBody().getBalance());
		System.out.println(accounts);
		
		
	}
	
	/*********************************************
   	 * method to view the transfer history 
   	 * 
   	 * used before sending or requesting a transfer 
   	 *********************************************/

	private void viewTransferHistory() {
		User user = currentUser.getUser();
		Account account = new Account();
		System.out.println("View transfer history");
		
		account = apiCall.getForObject(API_BASE_URL + "accounts/" + user.getId(), Account.class);
		int accountId = account.getAccountId();
		
		ResponseEntity<Transfer[]> responseEntity = apiCall.getForEntity(API_BASE_URL + "transfers/accounts/" + accountId, Transfer[].class);
		List<Transfer> currentTransfers = Arrays.asList(responseEntity.getBody());
		
		System.out.println("Transaction History: ");
		printTransfers(currentTransfers);
	}

	private void viewPendingRequests() {
		User user = currentUser.getUser();
		// TODO Auto-generated method stub
		
	}
	
	/*********************************************
   	 * Method to send money
   	 * make sure the user id is valid
   	 * make sure the amt is valid
   	 *********************************************/

	private void sendBucks() {
		User user = currentUser.getUser();

		ResponseEntity<User[]> responseEntity = apiCall.getForEntity(API_BASE_URL + "users", User[].class);
		List<User> users = Arrays.asList(responseEntity.getBody());
		
		// Prompt user for user to transfer money to and return result
		int userId = requestUserId(users);
		// Exit method if user enters 0
		if(userId == 0) {
			System.out.println("Exiting User Selection");
			return;
		}
		
		// Prompt user for money to be transfered and return result
		double money = requestMoneyToSend();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		// Create a transfer object to pass through the entity used in the API call
		Transfer entityTransfer = new Transfer();
		
		if(money > apiCall.getForObject(API_BASE_URL + "accounts/" + user.getId(), Account.class).getBalance()) {
			System.out.println("Money was greater than balance.  Send a lower amount next time?");
			// Create the transfer and set status to rejected since the user is poor
			entityTransfer = initTransfer(userId, user.getId(), money, 3, 2);
		}
		else {
			// Create the transfer and set status to accepted since the user has enough money
			entityTransfer = initTransfer(userId, user.getId(), money, 2, 2);
		}
		
		HttpEntity anEntity = new HttpEntity(entityTransfer, headers);
		Transfer transfer = apiCall.postForObject(API_BASE_URL + "transfers", anEntity, Transfer.class);
	}
	
	/*********************************************
   	 * Method to initialize a transfer
   	 *********************************************/
	
	private Transfer initTransfer(long toId, long fromId, double amount, int status, int type) {
		Transfer transfer = new Transfer();
		transfer.setAccountFrom((int)fromId);
		transfer.setAccountTo((int)toId);
		transfer.setAmount(amount);
		transfer.setTransferStatusId(status);
		transfer.setTransferTypeId(type);
		return transfer;
	}
	
	/*********************************************
   	 * Method to request buck (BONUS)
   	 *********************************************/

	private void requestBucks() {
		User user = currentUser.getUser();
		
		
		
	}
	
	/*********************************************
   	 * Method to request an exist ID from a list of users
   	 * Prompts for user input
   	 * Repeats until a valid id, or 0 is entered
   	 *********************************************/
	
	private int requestUserId(List<User> users) {
		// Print a list of users and ask the user to select one
		printUsers(users);
		int userId = console.getUserInputInteger("Please select an user or 0 to exit");
		// if the user picked a valid account let them transfer the money
		while (userId < 0 || userId > users.size()) {
			System.out.println("The id entered wasn't valid try again");
			userId = console.getUserInputInteger("Please select an user");
		}
		return userId;
	}
	
	/*********************************************
   	 * Method to request an amount of money to be sent from user
   	 * will repeat until user enters a positive value
   	 *********************************************/
	
	private double requestMoneyToSend() {
		double money = Double.parseDouble((String)console.getUserInput("How much money would you like to send"));
		
		while (money <= 0) {
			System.out.println("Do not enter a negetive amount of money to transfer!");
			money = Double.parseDouble((String)console.getUserInput("How much money would you like to send"));
		}
		return money;
	}
	
	/*********************************************
   	 * Method to print transfers in a readable format
   	 *********************************************/
	
	private void printTransfers(List<Transfer> transfers) {
		if(transfers.size() > 0) {
			for(Transfer aTransfer : transfers) {
				System.out.print(aTransfer.toString());
			}
		}
		
		
	}
	
	/*********************************************
   	 * Method to print users in a readable format
   	 *********************************************/
	
	private void printUsers(List<User> users) {
		if(users.size() > 0) {
			System.out.println("Printing out all registered users.");
			for(User anUser : users) {
				System.out.println("ID: " + anUser.getId() + " - " + anUser.toString());
			}
		}
		else {
			System.out.println("No users found");
		}
	}
	
	/*********************************************
   	 * Graceful exit
   	 *********************************************/
	
	private void exitProgram() {
		
		System.out.print("Exiting");
		for (int i = 0; i < 3; i++) {
			
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.print(".");
			
		}
		System.out.println("\n***************************");
		System.out.println("* Thanks for using TEnmo! *");
		System.out.println("***************************");
		System.exit(0);
	}


	
	/*********************************************
   	 * Methods below came with the project don't change!
   	 *********************************************/
	
	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
