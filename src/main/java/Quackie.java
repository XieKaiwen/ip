import java.io.IOException;
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
        Storage storage = new Storage();
        int taskCount = loadTasks(storage, tasks);

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(separator);

            if (command.isBlank()) {
                System.out.println(" OOPS!!! Please enter a command.");
            } else {
                switch (CommandType.fromInput(command)) {
                case BYE:
                    System.out.println("Bye. Hope to see you again soon!");
                    System.out.println(separator);
                    return;
                case LIST:
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println(" " + (i + 1) + "." + tasks[i]);
                    }
                    break;
                case DELETE:
                    int deleteIndex = getTaskIndex(command, "delete", taskCount);
                    if (deleteIndex >= 0) {
                        Task removedTask = tasks[deleteIndex];
                        for (int i = deleteIndex; i < taskCount - 1; i++) {
                            tasks[i] = tasks[i + 1];
                        }
                        tasks[--taskCount] = null;
                        saveTasks(storage, tasks, taskCount);
                        System.out.println(" Noted. I've removed this task:");
                        System.out.println("   " + removedTask);
                        System.out.println(" Now you have " + taskCount + " tasks in the list.");
                    } else {
                        System.out.println(" OOPS!!! Please provide a valid task number.");
                    }
                    break;
                case MARK:
                    int markIndex = getTaskIndex(command, "mark", taskCount);
                    if (markIndex >= 0) {
                        tasks[markIndex].markAsDone();
                        saveTasks(storage, tasks, taskCount);
                        System.out.println(" Nice! I've marked this task as done:");
                        System.out.println("   " + tasks[markIndex]);
                    } else {
                        System.out.println(" OOPS!!! Please provide a valid task number.");
                    }
                    break;
                case UNMARK:
                    int unmarkIndex = getTaskIndex(command, "unmark", taskCount);
                    if (unmarkIndex >= 0) {
                        tasks[unmarkIndex].markAsUndone();
                        saveTasks(storage, tasks, taskCount);
                        System.out.println(" OK, I've marked this task as not done yet:");
                        System.out.println("   " + tasks[unmarkIndex]);
                    } else {
                        System.out.println(" OOPS!!! Please provide a valid task number.");
                    }
                    break;
                case EVENT:
                    String eventDetails = command.substring("event".length()).trim();
                    int fromMarker = eventDetails.indexOf(" /from ");
                    int toMarker = eventDetails.indexOf(" /to ", fromMarker + " /from ".length());
                    String eventDescription = fromMarker >= 0
                            ? eventDetails.substring(0, fromMarker) : eventDetails;
                    String from = fromMarker >= 0 && toMarker >= 0
                            ? eventDetails.substring(fromMarker + " /from ".length(), toMarker) : "";
                    String to = toMarker >= 0 ? eventDetails.substring(toMarker + " /to ".length()) : "";
                    if (eventDescription.isBlank() || from.isBlank() || to.isBlank()) {
                        System.out.println(" OOPS!!! An event needs a description, /from time, and /to time.");
                    } else {
                        Task task = new Event(eventDescription.trim(), from.trim(), to.trim());
                        if (taskCount < MAX_TASKS) {
                            tasks[taskCount] = task;
                            taskCount++;
                        }
                        saveTasks(storage, tasks, taskCount);
                        System.out.println(" Got it. I've added this task:");
                        System.out.println("   " + task);
                        System.out.println(" Now you have " + taskCount + " tasks in the list.");
                    }
                    break;
                case DEADLINE:
                    String deadlineDetails = command.substring("deadline".length()).trim();
                    int byMarker = deadlineDetails.indexOf(" /by ");
                    String deadlineDescription = byMarker >= 0
                            ? deadlineDetails.substring(0, byMarker) : deadlineDetails;
                    String by = byMarker >= 0
                            ? deadlineDetails.substring(byMarker + " /by ".length()) : "";
                    if (deadlineDescription.isBlank() || by.isBlank()) {
                        System.out.println(" OOPS!!! A deadline needs a description and a /by date or time.");
                    } else {
                        try {
                            Task task = new Deadline(deadlineDescription.trim(), by.trim());
                            if (taskCount < MAX_TASKS) {
                                tasks[taskCount] = task;
                                taskCount++;
                            }
                            saveTasks(storage, tasks, taskCount);
                            System.out.println(" Got it. I've added this task:");
                            System.out.println("   " + task);
                            System.out.println(" Now you have " + taskCount + " tasks in the list.");
                        } catch (IllegalArgumentException exception) {
                            System.out.println(" OOPS!!! Please enter a valid deadline date (yyyy-MM-dd)"
                                    + " or date and time (d/M/yyyy HHmm).");
                        }
                    }
                    break;
                case TODO:
                    String description = command.substring("todo".length()).trim();
                    if (description.isBlank()) {
                        System.out.println(" OOPS!!! A ToDo needs a description.");
                    } else {
                        Task task = new ToDo(description);
                        if (taskCount < MAX_TASKS) {
                            tasks[taskCount] = task;
                            taskCount++;
                        }
                        saveTasks(storage, tasks, taskCount);
                        System.out.println(" Got it. I've added this task:");
                        System.out.println("   " + task);
                        System.out.println(" Now you have " + taskCount + " tasks in the list.");
                    }
                    break;
                case UNKNOWN:
                    System.out.println(" OOPS!!! I don't recognize that command.");
                    break;
                }
            }

            System.out.println(separator);
        }
    }

    /**
     * Loads saved tasks while keeping the chatbot usable when the data is invalid.
     *
     * @param storage the storage service to read from
     * @param tasks the array into which loaded tasks are placed
     * @return the number of tasks loaded, or zero when loading fails
     */
    private static int loadTasks(Storage storage, Task[] tasks) {
        try {
            return storage.load(tasks);
        } catch (IOException | RuntimeException exception) {
            System.out.println(" OOPS!!! I couldn't load saved tasks. Starting with an empty list.");
            return 0;
        }
    }

    /**
     * Saves tasks while keeping the chatbot usable when the data file is unavailable.
     *
     * @param storage the storage service to write to
     * @param tasks the array containing the tasks to save
     * @param taskCount the number of occupied entries in the array
     */
    private static void saveTasks(Storage storage, Task[] tasks, int taskCount) {
        try {
            storage.save(tasks, taskCount);
        } catch (IOException exception) {
            System.out.println(" OOPS!!! I couldn't save your tasks.");
        }
    }

    /**
     * Converts the task number in a mark, unmark, or delete command into a zero-based array index.
     *
     * @param command the complete command entered by the user
     * @param keyword the command keyword, such as {@code mark}, {@code unmark}, or {@code delete}
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
