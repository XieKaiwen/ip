package quackie.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import quackie.task.Deadline;
import quackie.task.Event;
import quackie.task.TaskList;
import quackie.task.ToDo;

/** Tests persistence of task types, details, and completion status. */
class StorageTest {
    @TempDir
    Path temporaryDirectory;

    /** Verifies that saved tasks can be loaded with their state intact. */
    @Test
    void savesAndLoadsAllTaskTypes() throws IOException {
        Path dataFile = temporaryDirectory.resolve("nested").resolve("tasks.txt");
        Storage storage = new Storage(dataFile);
        TaskList tasks = new TaskList();
        tasks.add(new ToDo("read book"));
        Deadline deadline = new Deadline("submit report", "2019-10-15");
        deadline.markAsDone();
        tasks.add(deadline);
        tasks.add(new Event("meeting", "2pm", "3pm"));

        storage.save(tasks);

        TaskList loaded = new TaskList();
        storage.load(loaded);
        assertTrue(Files.exists(dataFile));
        assertEquals(3, loaded.size());
        assertEquals("[T][ ] read book", loaded.get(0).toString());
        assertEquals("[D][X] submit report (by: Oct 15 2019)", loaded.get(1).toString());
        assertEquals("[E][ ] meeting (from: 2pm to: 3pm)", loaded.get(2).toString());
    }

    /** Verifies that loading a missing file leaves the list empty. */
    @Test
    void missingFileIsTreatedAsEmpty() throws IOException {
        Storage storage = new Storage(temporaryDirectory.resolve("missing.txt"));
        TaskList tasks = new TaskList();

        storage.load(tasks);

        assertFalse(Files.exists(temporaryDirectory.resolve("missing.txt")));
        assertEquals(0, tasks.size());
    }
}
