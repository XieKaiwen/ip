package quackie.task;

import java.util.Locale;

/**
 * Stores and manages the tasks in the current Quackie session.
 */
public class TaskList {
    private static final int MAX_TASKS = 100;

    private final Task[] tasks;
    private int taskCount;

    /** Creates an empty task list with capacity for up to 100 tasks. */
    public TaskList() {
        tasks = new Task[MAX_TASKS];
    }

    /**
     * Returns the number of tasks currently in the list.
     *
     * @return the number of stored tasks
     */
    public int size() {
        return taskCount;
    }

    /**
     * Adds a task when the list has capacity.
     *
     * @param task the task to add
     * @return {@code true} when the task was added, or {@code false} when the list is full
     */
    public boolean add(Task task) {
        if (taskCount >= tasks.length) {
            return false;
        }
        tasks[taskCount] = task;
        taskCount++;
        return true;
    }

    /**
     * Returns the task at a zero-based index.
     *
     * @param index the zero-based task index
     * @return the task at the requested index
     * @throws IndexOutOfBoundsException if the index is outside the list
     */
    public Task get(int index) {
        checkIndex(index);
        return tasks[index];
    }

    /**
     * Deletes and returns the task at a zero-based index.
     *
     * @param index the zero-based task index
     * @return the deleted task
     * @throws IndexOutOfBoundsException if the index is outside the list
     */
    public Task delete(int index) {
        checkIndex(index);
        Task removedTask = tasks[index];
        for (int i = index; i < taskCount - 1; i++) {
            tasks[i] = tasks[i + 1];
        }
        tasks[--taskCount] = null;
        return removedTask;
    }

    /**
     * Marks the task at a zero-based index as done.
     *
     * @param index the zero-based task index
     * @throws IndexOutOfBoundsException if the index is outside the list
     */
    public void markAsDone(int index) {
        get(index).markAsDone();
    }

    /**
     * Marks the task at a zero-based index as not done.
     *
     * @param index the zero-based task index
     * @throws IndexOutOfBoundsException if the index is outside the list
     */
    public void markAsUndone(int index) {
        get(index).markAsUndone();
    }

    /**
     * Returns tasks whose descriptions contain the supplied keyword, ignoring case.
     *
     * @param keyword the text to search for in task descriptions
     * @return a new task list containing the matching tasks in their original order
     */
    public TaskList find(String keyword) {
        String normalisedKeyword = keyword.toLowerCase(Locale.ROOT);
        TaskList matches = new TaskList();
        for (int i = 0; i < taskCount; i++) {
            String normalisedDescription = tasks[i].getDescription().toLowerCase(Locale.ROOT);
            if (normalisedDescription.contains(normalisedKeyword)) {
                matches.add(tasks[i]);
            }
        }
        return matches;
    }

    /**
     * Verifies that an index points to a stored task.
     *
     * @param index the zero-based task index to verify
     * @throws IndexOutOfBoundsException if the index is outside the list
     */
    private void checkIndex(int index) {
        if (index < 0 || index >= taskCount) {
            throw new IndexOutOfBoundsException("Invalid task index: " + index);
        }
    }
}
