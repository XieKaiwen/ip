package quackie.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Tests adding, retrieving, deleting, and updating tasks in a task list. */
class TaskListTest {
    /** Verifies list insertion, retrieval, completion changes, and deletion. */
    @Test
    void managesTasksInOrder() {
        TaskList tasks = new TaskList();
        Task first = new ToDo("first");
        Task second = new ToDo("second");

        assertTrue(tasks.add(first));
        assertTrue(tasks.add(second));
        assertEquals(2, tasks.size());
        assertEquals(first, tasks.get(0));
        tasks.markAsDone(1);
        assertTrue(tasks.get(1).isDone());
        tasks.markAsUndone(1);
        assertFalse(tasks.get(1).isDone());
        assertEquals(first, tasks.delete(0));
        assertEquals(second, tasks.get(0));
        assertEquals(1, tasks.size());
    }

    /** Verifies that the fixed capacity is enforced and indexes are checked. */
    @Test
    void enforcesCapacityAndValidIndexes() {
        TaskList tasks = new TaskList();
        for (int i = 0; i < 100; i++) {
            assertTrue(tasks.add(new ToDo("task " + i)));
        }

        assertFalse(tasks.add(new ToDo("too many")));
        assertThrows(IndexOutOfBoundsException.class, () -> tasks.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> tasks.delete(100));
    }
}
