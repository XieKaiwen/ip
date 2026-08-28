package quackie.task;

/**
 * A task that starts and ends at specified dates or times.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates a not-done event task.
     *
     * @param description the text describing the event
     * @param from the event start date or time
     * @param to the event end date or time
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    protected String getTypeIcon() {
        return "[E]";
    }

    /**
     * Returns the event's starting date or time.
     *
     * @return the event start value
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the event's ending date or time.
     *
     * @return the event end value
     */
    public String getTo() {
        return to;
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
