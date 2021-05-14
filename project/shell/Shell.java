package shell;

import java.util.Scanner;

public class Shell {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("> ");
        String cmd = input.nextLine();

        while (!cmd.equals("exit")) {
            switch(cmd) {
                case "help":
                printHelp();
                break;
                case "login":
                break;
            }
            System.out.print("> ");
            cmd = input.nextLine();
        }

        input.close();
    }

    public static void printHelp() {
        System.out.println("--- Available Commands ---");
        System.out.println("login [username] [password]");
    }
    
}
