package shell;

import java.util.Scanner;
import backend.*;

public class TraderShell {
    public static void main(String[] args) {
        Controller controller = new Controller();
        Scanner input = new Scanner(System.in);

        System.out.print("> ");
        String cmd = input.nextLine();

        while (!cmd.equals("q") && !cmd.equals("quit")) {
            switch(cmd) {
                case "help":
                printHelp();
                break;
            }
            System.out.print("> ");
            cmd = input.nextLine();
        }

        input.close();
    }
    
    // CLASSPATH=/home/ucsb/cs174a/cs174a-stock-project; export CLASSPATH

    public static void printHelp() {
        System.out.println("--- Available Commands ---");
        System.out.println("login [username] [password]");
    }
}
