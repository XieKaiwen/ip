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
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(separator);

            if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(separator);
                return;
            }

            if (command.isBlank()) {
                System.out.println(" OOPS!!! Please enter a command.");
            } else if (command.equals("list")) {
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i]);
                }
            } else if (command.equals("mark") || command.startsWith("mark ")) {
                int taskIndex = getTaskIndex(command, "mark", taskCount);
                if (taskIndex >= 0) {
                    tasks[taskIndex].markAsDone();
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + tasks[taskIndex]);
                } else {
                    System.out.println(" OOPS!!! Please provide a valid task number.");
                }
            } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                int taskIndex = getTaskIndex(command, "unmark", taskCount);
                if (taskIndex >= 0) {
                    tasks[taskIndex].markAsUndone();
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + tasks[taskIndex]);
                } else {
                    System.out.println(" OOPS!!! Please provide a valid task number.");
                }
            } else if (command.equals("event") || command.startsWith("event ")) {
                String details = command.substring("event".length()).trim();
                int fromMarker = details.indexOf(" /from ");
                int toMarker = details.indexOf(" /to ", fromMarker + " /from ".length());
                String description = fromMarker >= 0 ? details.substring(0, fromMarker) : details;
                String from = fromMarker >= 0 && toMarker >= 0
                        ? details.substring(fromMarker + " /from ".length(), toMarker) : "";
                String to = toMarker >= 0 ? details.substring(toMarker + " /to ".length()) : "";
                if (description.isBlank() || from.isBlank() || to.isBlank()) {
                    System.out.println(" OOPS!!! An event needs a description, /from time, and /to time.");
                } else {
                    Task task = new Event(description.trim(), from.trim(), to.trim());
                    if (taskCount < MAX_TASKS) {
                        tasks[taskCount] = task;
                        taskCount++;
                    }
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + task);
                    System.out.println(" Now you have " + taskCount + " tasks in the list.");
                }
            } else if (command.equals("deadline") || command.startsWith("deadline ")) {
                String details = command.substring("deadline".length()).trim();
                int byMarker = details.indexOf(" /by ");
                String description = byMarker >= 0 ? details.substring(0, byMarker) : details;
                String by = byMarker >= 0 ? details.substring(byMarker + " /by ".length()) : "";
                if (description.isBlank() || by.isBlank()) {
                    System.out.println(" OOPS!!! A deadline needs a description and a /by date or time.");
                } else {
                    Task task = new Deadline(description.trim(), by.trim());
                    if (taskCount < MAX_TASKS) {
                        tasks[taskCount] = task;
                        taskCount++;
                    }
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + task);
                    System.out.println(" Now you have " + taskCount + " tasks in the list.");
                }
            } else if (command.equals("todo") || command.startsWith("todo ")) {
                String description = command.substring("todo".length()).trim();
                if (description.isBlank()) {
                    System.out.println(" OOPS!!! A ToDo needs a description.");
                } else {
                    Task task = new ToDo(description);
                    if (taskCount < MAX_TASKS) {
                        tasks[taskCount] = task;
                        taskCount++;
                    }
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + task);
                    System.out.println(" Now you have " + taskCount + " tasks in the list.");
                }
            } else {
                System.out.println(" OOPS!!! I don't recognize that command.");
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
