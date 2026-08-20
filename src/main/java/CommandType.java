/**
 * The command keywords understood by Quackie.
 */
public enum CommandType {
    /** Terminates the current session. */
    BYE,
    /** Lists all stored tasks. */
    LIST,
    /** Deletes one stored task. */
    DELETE,
    /** Marks one stored task as done. */
    MARK,
    /** Marks one stored task as not done. */
    UNMARK,
    /** Adds an event task. */
    EVENT,
    /** Adds a deadline task. */
    DEADLINE,
    /** Adds a ToDo task. */
    TODO,
    /** Represents an input that is not a recognised command. */
    UNKNOWN;

    /**
     * Identifies the command keyword at the start of a raw user input.
     *
     * @param input the complete command entered by the user
     * @return the matching command type, or {@link #UNKNOWN} if none matches
     */
    public static CommandType fromInput(String input) {
        if (input.equals("bye")) {
            return BYE;
        } else if (input.equals("list")) {
            return LIST;
        } else if (input.equals("delete") || input.startsWith("delete ")) {
            return DELETE;
        } else if (input.equals("mark") || input.startsWith("mark ")) {
            return MARK;
        } else if (input.equals("unmark") || input.startsWith("unmark ")) {
            return UNMARK;
        } else if (input.equals("event") || input.startsWith("event ")) {
            return EVENT;
        } else if (input.equals("deadline") || input.startsWith("deadline ")) {
            return DEADLINE;
        } else if (input.equals("todo") || input.startsWith("todo ")) {
            return TODO;
        }
        return UNKNOWN;
    }
}
