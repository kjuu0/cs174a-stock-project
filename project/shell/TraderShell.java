package shell;

import java.util.Scanner;
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


            }
            System.out.print("> ");
            cmd = input.nextLine();
        }

        input.close();
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
