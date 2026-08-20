import java.util.Scanner;

/**
 * Entry point for the Quackie chatbot.
 */
public class Quackie {
    /** Maximum number of tasks that can be stored during one session. */
    private static final int MAX_TASKS = 100;

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
        String[] tasks = new String[MAX_TASKS];
        boolean[] taskDone = new boolean[MAX_TASKS];
        int taskCount = 0;

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(separator);

            if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(separator);
                return;
            }

            if (command.equals("list")) {
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    String statusIcon = taskDone[i] ? "X" : " ";
                    System.out.println(" " + (i + 1) + ".[" + statusIcon + "] " + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                int taskIndex = getTaskIndex(command, "mark", taskCount);
                if (taskIndex >= 0) {
                    taskDone[taskIndex] = true;
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   [X] " + tasks[taskIndex]);
                } else {
                    System.out.println(" I couldn't find that task.");
                }
            } else if (command.startsWith("unmark ")) {
                int taskIndex = getTaskIndex(command, "unmark", taskCount);
                if (taskIndex >= 0) {
                    taskDone[taskIndex] = false;
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   [ ] " + tasks[taskIndex]);
                } else {
                    System.out.println(" I couldn't find that task.");
                }
            } else {
                if (taskCount < MAX_TASKS) {
                    tasks[taskCount] = command;
                    taskCount++;
                }
                System.out.println(" added: " + command);
            }

            System.out.println(separator);
        }
    }

    /**
     * Converts the task number in a mark or unmark command into a zero-based array index.
     *
     * @param command the complete command entered by the user
     * @param keyword the command keyword, either {@code mark} or {@code unmark}
     * @param taskCount the number of tasks currently stored
     * @return the corresponding zero-based index, or {@code -1} for an invalid number
     */
    private static int getTaskIndex(String command, String keyword, int taskCount) {
        try {
            int taskNumber = Integer.parseInt(command.substring(keyword.length()).trim());
            return taskNumber >= 1 && taskNumber <= taskCount ? taskNumber - 1 : -1;
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
}
