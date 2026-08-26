import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

/**
 * Persists Quackie's tasks in a local text file.
 *
 * <p>Each record stores a task type, completion status, and Base64-encoded
 * task details separated by pipe characters. Encoding the details keeps
 * descriptions and dates safe when they contain the separator.
 */
public class Storage {
    private static final Path DATA_FILE = Path.of("data", "quackie.txt");
    private static final String FIELD_SEPARATOR = "|";

    /**
     * Saves the current tasks to the data file.
     *
     * @param tasks the array containing the tasks to save
     * @param taskCount the number of occupied entries in the array
     * @throws IOException if the data directory or file cannot be written
     */
    public void save(Task[] tasks, int taskCount) throws IOException {
        Path parentDirectory = DATA_FILE.getParent();
        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(DATA_FILE, StandardCharsets.UTF_8)) {
            for (int i = 0; i < taskCount; i++) {
                writer.write(serialize(tasks[i]));
                writer.newLine();
            }
        }
    }

    /**
     * Converts one task into its storage record.
     *
     * @param task the task to convert
     * @return a pipe-separated record suitable for writing to the data file
     */
    private String serialize(Task task) {
        String status = task.isDone() ? "1" : "0";
        String description = encode(task.getDescription());

        if (task instanceof Deadline deadline) {
            return String.join(FIELD_SEPARATOR, "D", status, description,
                    encode(deadline.getBy()));
        }

        if (task instanceof Event event) {
            return String.join(FIELD_SEPARATOR, "E", status, description,
                    encode(event.getFrom()), encode(event.getTo()));
        }

        return String.join(FIELD_SEPARATOR, "T", status, description);
    }

    /**
     * Encodes text so that separators in user input cannot corrupt a record.
     *
     * @param value the text to encode
     * @return the Base64 representation of the text
     */
    private String encode(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }
}
