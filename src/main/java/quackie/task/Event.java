package quackie.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * A task that starts and ends at specified dates or times.
 */
public class Event extends Task {
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter
            .ofPattern("d/M/uuuu HHmm", Locale.ENGLISH)
            .withResolverStyle(ResolverStyle.STRICT);
    private static final String DATE_TIME_PATTERN = "\\d{1,2}/\\d{1,2}/\\d{4} \\d{4}";

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
        validateTimeRange(from, to);
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
    boolean hasSameDetails(Task other) {
        return super.hasSameDetails(other)
                && from.equalsIgnoreCase(((Event) other).from)
                && to.equalsIgnoreCase(((Event) other).to);
    }

    /** Validates structured event times while retaining support for free-form values. */
    private void validateTimeRange(String from, String to) {
        boolean hasStructuredStart = from.matches(DATE_TIME_PATTERN);
        boolean hasStructuredEnd = to.matches(DATE_TIME_PATTERN);
        if (!hasStructuredStart && !hasStructuredEnd) {
            return;
        }
        if (!hasStructuredStart || !hasStructuredEnd) {
            throw new IllegalArgumentException(
                    "Both event times must use d/M/yyyy HHmm when either one does.");
        }

        try {
            LocalDateTime startTime = LocalDateTime.parse(from, DATE_TIME_FORMAT);
            LocalDateTime endTime = LocalDateTime.parse(to, DATE_TIME_FORMAT);
            if (!startTime.isBefore(endTime)) {
                throw new IllegalArgumentException("The event end time must be after its start time.");
            }
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Please enter valid event times using d/M/yyyy HHmm.");
        }
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
