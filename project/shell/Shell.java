import java.util.Scanner;

public class Shell {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        String cmd = input.nextLine();

        while (cmd != "exit") {
            switch(cmd) {
                case "help":
                printHelp();
                break;
            }
            cmd = input.nextLine();
        }

        input.close();
    }

    public static void printHelp() {
        System.out.println("list of commands");
    }
}
