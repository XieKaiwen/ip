package quackie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import quackie.storage.Storage;

/**
 * Tests the command-response interface used by the JavaFX GUI.
 */
class QuackieTest {
    @TempDir
    private Path temporaryDirectory;

    /** Verifies that GUI commands share task state and return readable responses. */
    @Test
    void getResponseExecutesCommandsAndPreservesState() {
        Path dataFile = temporaryDirectory.resolve("quackie.txt");
        Quackie quackie = new Quackie(new Storage(dataFile));

        String addResponse = quackie.getResponse("todo read book");
        String markResponse = quackie.getResponse("mark 1");
        String listResponse = quackie.getResponse("list");

        assertTrue(addResponse.contains("I've added this task"));
        assertTrue(markResponse.contains("marked this task as done"));
        assertTrue(listResponse.contains("1.[T][X] read book"));
    }

    /** Verifies GUI-specific formatting and exit-command recognition. */
    @Test
    void getResponseFormatsErrorsAndExitMessage() {
        Quackie quackie = new Quackie(new Storage(temporaryDirectory.resolve("commands.txt")));

        assertEquals("OOPS!!! Please enter a command.", quackie.getResponse(""));
        assertEquals("Bye. Hope to see you again soon!", quackie.getResponse("bye"));
        assertTrue(quackie.isExitCommand("bye"));
    }
}
