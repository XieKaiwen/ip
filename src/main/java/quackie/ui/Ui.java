package quackie.ui;

import java.io.PrintStream;
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
    private final PrintStream output;

    /** Creates a console UI that reads commands from standard input. */
    public Ui() {
        this(new Scanner(System.in), System.out);
    }

    /**
     * Creates a UI with caller-supplied streams, which allows output to be captured in tests.
     *
     * @param scanner the source of user commands
     * @param output the destination for chatbot messages
     */
    public Ui(Scanner scanner, PrintStream output) {
        this.scanner = scanner;
        this.output = output;
    }

    /** Displays the welcome banner and prompt. */
    public void showWelcome() {
        showLine();
        output.println(BANNER);
        output.println("Hello! I'm Quackie.");
        output.println("What can I do for you?");
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
        output.println(SEPARATOR);
    }

    /** Displays the farewell message and trailing separator. */
    public void showBye() {
        output.println("Bye. Hope to see you again soon!");
        showLine();
    }

    /**
     * Displays an error message using Quackie's standard error prefix.
     *
     * @param message the explanation to show to the user
     */
    public void showError(String message) {
        output.println(" OOPS!!! " + message);
    }

    /**
     * Displays every task in the supplied list.
     *
     * @param tasks the tasks to display
     */
    public void showTasks(TaskList tasks) {
        output.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            output.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays confirmation for a newly added task.
     *
     * @param task the task that was added
     * @param taskCount the number of tasks after the addition
     */
    public void showTaskAdded(Task task, int taskCount) {
        output.println(" Got it. I've added this task:");
        output.println("   " + task);
        output.println(" Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays confirmation for a deleted task.
     *
     * @param task the task that was deleted
     * @param taskCount the number of tasks after the deletion
     */
    public void showTaskDeleted(Task task, int taskCount) {
        output.println(" Noted. I've removed this task:");
        output.println("   " + task);
        output.println(" Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays confirmation for a task marked as done.
     *
     * @param task the task that was marked as done
     */
    public void showTaskMarked(Task task) {
        output.println(" Nice! I've marked this task as done:");
        output.println("   " + task);
    }

    /**
     * Displays confirmation for a task marked as not done.
     *
     * @param task the task that was marked as not done
     */
    public void showTaskUnmarked(Task task) {
        output.println(" OK, I've marked this task as not done yet:");
        output.println("   " + task);
    }
}
