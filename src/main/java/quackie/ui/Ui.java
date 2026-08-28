package quackie.ui;

import java.util.Scanner;

import quackie.task.Task;
import quackie.task.TaskList;

/**
 * Handles Quackie's console input and output.
 */
public class Ui {
    private static final String SEPARATOR = "_".repeat(60);
    private static final String BANNER = "                           _      _      \n"
            + "  __ _  _   _   __ _  ___| | __ (_)  ___ \n"
            + " / _` || | | | / _` |/ __| |/ / | | / _ \\ \n"
            + "| (_| || |_| || (_| | (__|   <  | ||  __/\n"
            + " \\__, | \\__,_| \\__,_|\\___|_|\\_\\ |_|\\___|\n"
            + "    |_|                                  ";

    private final Scanner scanner;

    /** Creates a console UI that reads commands from standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Displays the welcome banner and prompt. */
    public void showWelcome() {
        showLine();
        System.out.println(BANNER);
        System.out.println("Hello! I'm Quackie.");
        System.out.println("What can I do for you?");
        showLine();
    }

    /**
     * Reads the next command from standard input.
     *
     * @return the next command, or {@code null} when input has ended
     */
    public String readCommand() {
        return scanner.hasNextLine() ? scanner.nextLine() : null;
    }

    /** Displays the standard horizontal separator. */
    public void showLine() {
        System.out.println(SEPARATOR);
    }

    /** Displays the farewell message and trailing separator. */
    public void showBye() {
        System.out.println("Bye. Hope to see you again soon!");
        showLine();
    }

    /**
     * Displays an error message using Quackie's standard error prefix.
     *
     * @param message the explanation to show to the user
     */
    public void showError(String message) {
        System.out.println(" OOPS!!! " + message);
    }

    /**
     * Displays every task in the supplied list.
     *
     * @param tasks the tasks to display
     */
    public void showTasks(TaskList tasks) {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays confirmation for a newly added task.
     *
     * @param task the task that was added
     * @param taskCount the number of tasks after the addition
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays confirmation for a deleted task.
     *
     * @param task the task that was deleted
     * @param taskCount the number of tasks after the deletion
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays confirmation for a task marked as done.
     *
     * @param task the task that was marked as done
     */
    public void showTaskMarked(Task task) {
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
    }

    /**
     * Displays confirmation for a task marked as not done.
     *
     * @param task the task that was marked as not done
     */
    public void showTaskUnmarked(Task task) {
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
    }
}
