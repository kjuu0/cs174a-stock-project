package shell;

import java.util.Scanner;
import java.util.List;

import backend.*;

public class TraderShell {
    private static Scanner input;
    private static Controller controller;
    public static void main(String[] args) {
        controller = new Controller();
        input = new Scanner(System.in);

        System.out.print("> ");
        String cmd = input.nextLine();

        while (!cmd.equals("q") && !cmd.equals("quit")) {
            switch(cmd) {
                case "help":
                printHelp();
                break;
                case "signup":
                promptSignup();
                break;
                case "login":
                promptLogin();
                break;
                case "logout":
                logout();
                break;
                case "reset":
                controller.resetDatastore();
                break;
                case "buy":
                promptBuy();
                break;


            }
            System.out.print("> ");
            cmd = input.nextLine();
        }

        input.close();
    }

    public static void promptBuy() {
        if (!controller.isLoggedIn()) {
            System.out.println("You must be logged in to make a purchase");
            return;
        } 
        if (!controller.isMarketOpen()) {
            System.out.println("Market is closed, no purchases allowed"); 
            return;
        }

        System.out.println("Currently available stocks:");
        List<Stock> availableStocks = controller.getAvailableStocks(); 
        if (availableStocks.isEmpty()) {
            System.out.println("No stocks are available"); 
            return;
        }

        for (Stock s : availableStocks) {
            System.out.println(s); 
        }
        
        System.out.print("Enter the stock symbol to purchase: ");
        String symbol = input.nextLine();
       
        // For demo purposes assuming that there are not that many
        // stocks, we can linearly search; in the future we can modify
        // availableStocks to return a HashSet for a faster check
        boolean found = false;
        Stock tbp = null;
        for (Stock s : availableStocks) {
            if (s.getSymbol().equals(symbol)) {
                found = true;
                tbp = s;
                break; 
            }
        }
        
        if (tbp == null) {
            System.out.println("Invalid stock symbol"); 
            return;
        }
        
        System.out.print("Enter the number of shares to purchase: ");
        int numShares = input.nextInt();
        
        boolean res = controller.purchase(tbp, numShares);
        if (res) {
            System.out.println("Purchase successful"); 
        } else {
            System.out.println("Error occurred while attempting to purchase"); 
        }
        
    }

    public static void promptSignup() {
        Customer customer = new Customer();
        System.out.print("Name: ");
        customer.name = input.nextLine();
        System.out.print("Username: ");
        customer.username = input.nextLine();
        System.out.print("Password: ");
        customer.password = input.nextLine();
        System.out.print("Address: ");
        customer.address = input.nextLine();
        System.out.print("State: ");
        customer.state = input.nextLine();
        System.out.print("Phone: ");
        customer.phone = input.nextLine();
        System.out.print("Email: ");
        customer.email = input.nextLine();
        System.out.print("Tax Id: ");
        customer.taxid = Integer.parseInt(input.nextLine());
        System.out.print("SSN: ");
        customer.ssn = input.nextLine();
        boolean res = controller.createTrader(customer);
        
        if (!res) {
            System.out.println("Failed to create new user: " + String.join(",", new String[]{customer.name, customer.username, customer.password, customer.address, customer.state, customer.email, String.valueOf(customer.taxid), customer.ssn}));
        } else {
            System.out.println("Created new user with username " + customer.username + "!");
        }
    }

    public static void promptLogin() {
        System.out.print("Username: "); 
        String user = input.nextLine();
        System.out.print("Password: ");
        String pass = input.nextLine();
        controller.authenticateTrader(user, pass);
    }

    public static void logout() {
        boolean res = controller.logout();
        if (!res) {
            System.out.println("You are not logged in to any account.");
        } else {
            System.out.println("You have been logged out.");
        }
    }

    public static void printHelp() {
        System.out.println("--- Available Commands ---");
        System.out.println("login [username] [password]");
        System.out.println("logut");
        System.out.println("signup");
    }
}
