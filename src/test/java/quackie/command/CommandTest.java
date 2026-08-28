package quackie.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Scanner;

import quackie.storage.Storage;
import quackie.task.TaskList;
import quackie.task.ToDo;
import quackie.ui.Ui;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests command execution and its delegation to application services. */
class CommandTest {
    @TempDir
    Path temporaryDirectory;

    /** Verifies add, list, mark, unmark, and delete command effects. */
    @Test
    void executesTaskCommands() {
        TaskList tasks = new TaskList();
        Ui ui = createUi();
        Storage storage = new Storage(temporaryDirectory.resolve("tasks.txt"));

        new AddCommand(new ToDo("read book")).execute(tasks, ui, storage);
        assertEquals(1, tasks.size());
        new MarkCommand(0).execute(tasks, ui, storage);
        assertTrue(tasks.get(0).isDone());
        new UnmarkCommand(0).execute(tasks, ui, storage);
        assertFalse(tasks.get(0).isDone());
        new ListCommand().execute(tasks, ui, storage);
        new DeleteCommand(0).execute(tasks, ui, storage);
        assertEquals(0, tasks.size());
    }

    /** Verifies exit and unknown commands expose their control-flow behaviour. */
    @Test
    void identifiesExitAndUnknownCommands() {
        Ui ui = createUi();
        Storage storage = new Storage(temporaryDirectory.resolve("tasks.txt"));
        TaskList tasks = new TaskList();
        Command exit = new ExitCommand();

        assertTrue(exit.isExit());
        assertFalse(new UnknownCommand().isExit());
        exit.execute(tasks, ui, storage);
        new UnknownCommand().execute(tasks, ui, storage);
        new DeleteCommand(-1).execute(tasks, ui, storage);
        new MarkCommand(-1).execute(tasks, ui, storage);
        new UnmarkCommand(-1).execute(tasks, ui, storage);
    }

    /** Creates an in-memory UI for command tests. */
    private Ui createUi() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        return new Ui(new Scanner(""), new PrintStream(output, true, StandardCharsets.UTF_8));
    }
}
