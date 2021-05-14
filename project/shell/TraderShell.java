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
    }
}
