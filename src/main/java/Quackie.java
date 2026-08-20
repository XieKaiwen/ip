import java.util.Scanner;

/**
 * Entry point for the Quackie chatbot.
 */
public class Quackie {
    public static void main(String[] args) {
        String separator = "_".repeat(60);
        String banner = "                           _      _      \n"
                + "  __ _  _   _   __ _  ___| | __ (_)  ___ \n"
                + " / _` || | | | / _` |/ __| |/ / | | / _ \\ \n"
                + "| (_| || |_| || (_| | (__|   <  | ||  __/\n"
                + " \\__, | \\__,_| \\__,_|\\___|_|\\_\\ |_|\\___|\n"
                + "    |_|                                  ";

        System.out.println(separator);
        System.out.println(banner);
        System.out.println("Hello! I'm Quackie.");
        System.out.println("What can I do for you?");
        System.out.println(separator);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(separator);

            if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(separator);
                return;
            }

            System.out.println(" " + command);
            System.out.println(separator);
        }
    }
}
