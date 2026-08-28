package quackie.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import quackie.CommandType;
import quackie.command.AddCommand;
import quackie.command.Command;
import quackie.command.DeleteCommand;
import quackie.command.ExitCommand;
import quackie.command.FindCommand;
import quackie.command.ListCommand;
import quackie.command.MarkCommand;
import quackie.command.UnknownCommand;
import quackie.command.UnmarkCommand;
import quackie.task.Deadline;
import quackie.task.Event;
import quackie.task.TaskList;
import quackie.task.ToDo;

/** Tests conversion from raw input into command objects and task objects. */
class ParserTest {
    private final Parser parser = new Parser();

    /** Verifies command object creation for each command family. */
    @Test
    void createsExecutableCommands() {
        TaskList tasks = new TaskList();

        assertInstanceOf(ExitCommand.class, parser.parse("bye", tasks));
        assertInstanceOf(ListCommand.class, parser.parse("list", tasks));
        assertInstanceOf(FindCommand.class, parser.parse("find book", tasks));
        assertInstanceOf(DeleteCommand.class, parser.parse("delete 1", tasks));
        assertInstanceOf(MarkCommand.class, parser.parse("mark 1", tasks));
        assertInstanceOf(UnmarkCommand.class, parser.parse("unmark 1", tasks));
        assertInstanceOf(AddCommand.class, parser.parse("todo read book", tasks));
        assertInstanceOf(UnknownCommand.class, parser.parse("blah", tasks));
    }

    /** Verifies parsing of each supported task type and task-number validation. */
    @Test
    void parsesTasksAndIndexes() {
        TaskList tasks = new TaskList();
        tasks.add(new ToDo("existing"));

        assertEquals(CommandType.EVENT, parser.parseCommandType("event meeting /from 2pm /to 3pm"));
        assertInstanceOf(Event.class, parser.parseTask("event meeting /from 2pm /to 3pm"));
        assertInstanceOf(Deadline.class, parser.parseTask("deadline report /by Friday"));
        assertEquals(0, parser.parseTaskIndex("mark 1", "mark", tasks));
        assertEquals(-1, parser.parseTaskIndex("mark 0", "mark", tasks));
        assertEquals(-1, parser.parseTaskIndex("mark nope", "mark", tasks));
    }

    /** Verifies malformed task commands produce useful validation failures. */
    @Test
    void rejectsMalformedTasks() {
        assertThrows(IllegalArgumentException.class, () -> parser.parseTask("todo"));
        assertThrows(IllegalArgumentException.class, () -> parser.parseTask("event meeting"));
        assertThrows(IllegalArgumentException.class,
                () -> parser.parseTask("deadline report /by 2019-02-30"));
        assertThrows(IllegalArgumentException.class, () -> parser.parse("find", new TaskList()));
    }
}
