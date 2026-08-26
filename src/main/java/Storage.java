import java.io.BufferedReader;
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
     * Loads saved tasks into the supplied array.
     *
     * @param tasks the array into which loaded tasks are placed
     * @return the number of tasks loaded
     * @throws IOException if the data file cannot be read
     */
    public int load(Task[] tasks) throws IOException {
        if (!Files.exists(DATA_FILE)) {
            return 0;
        }

        int taskCount = 0;
        try (BufferedReader reader = Files.newBufferedReader(DATA_FILE, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null && taskCount < tasks.length) {
                tasks[taskCount] = deserialize(line);
                taskCount++;
            }
        }
        return taskCount;
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

    /**
     * Converts one storage record back into a task.
     *
     * @param line the storage record to convert
     * @return the task represented by the record
     */
    private Task deserialize(String line) {
        String[] fields = line.split("\\|", -1);
        Task task = switch (fields[0]) {
        case "D" -> new Deadline(decode(fields[2]), decode(fields[3]));
        case "E" -> new Event(decode(fields[2]), decode(fields[3]), decode(fields[4]));
        case "T" -> new ToDo(decode(fields[2]));
        default -> throw new IllegalArgumentException("Unknown task type: " + fields[0]);
        };

        if ("1".equals(fields[1])) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Decodes Base64 text from a storage record.
     *
     * @param value the encoded text
     * @return the decoded text
     */
    private String decode(String value) {
        return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
    }
}
