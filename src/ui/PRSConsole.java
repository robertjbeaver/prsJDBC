package ui;

import java.util.HashMap;
import java.util.List;

import business.User;
import business.UserDB;
import business.VendorDB;
import business.Vendor;
import business.Product;
import business.ProductDB;
import consolehelper.util.Console;

public class PRSConsole {
	private static UserDB userDB = new UserDB();
	private static VendorDB vendorDB = new VendorDB();
	private static ProductDB productDB = new ProductDB();
	
	private static HashMap<Integer, Vendor> vendorMap = new HashMap<>();
	
	public static void main(String[] args) {
		System.out.println("Welcome to the PRS Console application\n---------------------------------------");
		
		// populates vendorMap instance variable for use later
		populateVendorMap();
		
		String choice = "";
		do {
			choice = Console.getString(displayWelcomeMessage());
			if(choice.equalsIgnoreCase("list")) {
				List<User> users = userDB.list();
				for(User u:users) {
					System.out.println(u);
				}
			}else if(choice.equalsIgnoreCase("get")) {
				int id =Console.getInt("Enter User ID: ");
				User u = userDB.get(id);
				System.out.println(u);
				
			}else if (choice.equalsIgnoreCase("add")) {
				User u = addUser();
				if(userDB.add(u)) {
					System.out.println(u.getFirstName() + " added succesfully.");
				} else {
					System.out.println("Error");
				}
			}else if (choice.equalsIgnoreCase("remove")) {
				int id = Console.getInt("Enter id to delete");
				User u = userDB.get(id);
				if(userDB.remove(u)) {
					System.out.println(u.getFirstName()+ "removed");
				} else {
					System.out.println("Error");
				}
			
			} else if (choice.equalsIgnoreCase("product")){
				List<Product> products = productDB.list();
				for(Product p:products) {
					int vID = p.getVendorID();
					Vendor v = vendorMap.get(vID);
					p.setVendor(v);
					System.out.println(p);
				}
			}else if(choice.equalsIgnoreCase("vendor")) {
				List<Vendor> vendors = vendorDB.list();
				for(Vendor v:vendors) {
					System.out.println(v);
				}
			}else {
				System.out.println("Error...Invalid menu option\n");
			}
			
			
		} while(!choice.equalsIgnoreCase("exit"));
		
		System.out.println("Goodbye!");
	}

	private static User addUser() {
		String userName = Console.getString("Enter User Name");
		String password = Console.getString("Enter user password");
		String firstName = Console.getString("Enter first name");
		String lastName = Console.getString("Enter last name");
		String phoneNumber = Console.getString("Enter phone number");
		String email = Console.getString("Enter email");
		boolean reviewer = false;
		boolean admin = false;
		User u = new User(userName, password, firstName, lastName, phoneNumber, email, reviewer, admin);
		return u;
	}
	
	private static String displayWelcomeMessage() {
		String msg = "---MENU---\n--OPTIONS--\n" +
		"list = list all users\n"+
		"get = get a user by ID\n"+
		"add = add a user\n"+
		"remove = remove a user\n"+
		"exit = exit the application\n"+
		">";
		return msg;
		
	}
	private static void populateVendorMap() {
		List<Vendor> vendors = vendorDB.list();
		for(Vendor v:vendors) {
			vendorMap.put(v.getId(), v);
		}
	}
	private static void listProducts() {
		
	}
}
