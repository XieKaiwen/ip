/**
 * Interprets raw commands and creates the corresponding task objects.
 */
public class Parser {
    /**
     * Creates an executable command from raw user input.
     *
     * @param command the complete command entered by the user
     * @param tasks the current task list, used to validate task indexes
     * @return the executable command represented by the input
     * @throws IllegalArgumentException if a task-creation command is malformed
     */
    public Command parse(String command, TaskList tasks) {
        return switch (parseCommandType(command)) {
        case BYE -> new ExitCommand();
        case LIST -> new ListCommand();
        case DELETE -> new DeleteCommand(parseTaskIndex(command, "delete", tasks));
        case MARK -> new MarkCommand(parseTaskIndex(command, "mark", tasks));
        case UNMARK -> new UnmarkCommand(parseTaskIndex(command, "unmark", tasks));
        case EVENT, DEADLINE, TODO -> new AddCommand(parseTask(command));
        case UNKNOWN -> new UnknownCommand();
        };
    }

    /**
     * Identifies the command represented by raw user input.
     *
     * @param input the complete command entered by the user
     * @return the command type represented by the input
     */
    public CommandType parseCommandType(String input) {
        return CommandType.fromInput(input);
    }

    /**
     * Creates a task from a task-creation command.
     *
     * @param command the complete task-creation command
     * @return the task described by the command
     * @throws IllegalArgumentException if the command is malformed
     */
    public Task parseTask(String command) {
        return switch (parseCommandType(command)) {
        case EVENT -> parseEvent(command);
        case DEADLINE -> parseDeadline(command);
        case TODO -> parseToDo(command);
        default -> throw new IllegalArgumentException("Command does not create a task");
        };
    }

    /**
     * Converts a task number in a mark, unmark, or delete command into a zero-based index.
     *
     * @param command the complete command entered by the user
     * @param keyword the command keyword, such as {@code mark}, {@code unmark}, or {@code delete}
     * @param tasks the current task list
     * @return the corresponding zero-based index, or {@code -1} for an invalid task number
     */
    public int parseTaskIndex(String command, String keyword, TaskList tasks) {
        try {
            int taskNumber = Integer.parseInt(command.substring(keyword.length()).trim());
            return taskNumber >= 1 && taskNumber <= tasks.size() ? taskNumber - 1 : -1;
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    /**
     * Parses an event command and validates its required details.
     *
     * @param command the complete event command
     * @return the parsed event task
     * @throws IllegalArgumentException if the description or time details are missing
     */
    private Task parseEvent(String command) {
        String eventDetails = command.substring("event".length()).trim();
        int fromMarker = eventDetails.indexOf(" /from ");
        int toMarker = eventDetails.indexOf(" /to ", fromMarker + " /from ".length());
        String eventDescription = fromMarker >= 0
                ? eventDetails.substring(0, fromMarker) : eventDetails;
        String from = fromMarker >= 0 && toMarker >= 0
                ? eventDetails.substring(fromMarker + " /from ".length(), toMarker) : "";
        String to = toMarker >= 0 ? eventDetails.substring(toMarker + " /to ".length()) : "";
        if (eventDescription.isBlank() || from.isBlank() || to.isBlank()) {
            throw new IllegalArgumentException("An event needs a description, /from time, and /to time.");
        }
        return new Event(eventDescription.trim(), from.trim(), to.trim());
    }

    /**
     * Parses a deadline command and validates its required details.
     *
     * @param command the complete deadline command
     * @return the parsed deadline task
     * @throws IllegalArgumentException if the description or deadline value is invalid
     */
    private Task parseDeadline(String command) {
        String deadlineDetails = command.substring("deadline".length()).trim();
        int byMarker = deadlineDetails.indexOf(" /by ");
        String deadlineDescription = byMarker >= 0
                ? deadlineDetails.substring(0, byMarker) : deadlineDetails;
        String by = byMarker >= 0
                ? deadlineDetails.substring(byMarker + " /by ".length()) : "";
        if (deadlineDescription.isBlank() || by.isBlank()) {
            throw new IllegalArgumentException("A deadline needs a description and a /by date or time.");
        }
        try {
            return new Deadline(deadlineDescription.trim(), by.trim());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Please enter a valid deadline date (yyyy-MM-dd)"
                    + " or date and time (d/M/yyyy HHmm).");
        }
    }

    /**
     * Parses a ToDo command and validates its description.
     *
     * @param command the complete ToDo command
     * @return the parsed ToDo task
     * @throws IllegalArgumentException if the description is empty
     */
    private Task parseToDo(String command) {
        String description = command.substring("todo".length()).trim();
        if (description.isBlank()) {
            throw new IllegalArgumentException("A ToDo needs a description.");
        }
        return new ToDo(description);
    }
}
