/**
 * A task that must be completed by a specified date or time.
 */
public class Deadline extends Task {
    private String by;

    /**
     * Creates a not-done deadline task.
     *
     * @param description the text describing the task
     * @param by the date or time by which the task should be completed
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    protected String getTypeIcon() {
        return "[D]";
    }

    /**
     * Returns the date or time by which this task should be completed.
     *
     * @return the deadline value
     */
    public String getBy() {
        return by;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}
