package quackie.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Tests the task-specific fields and display values of ToDos and events. */
class TaskSubtypeTest {
    /** Verifies ToDo type formatting. */
    @Test
    void formatsToDo() {
        assertEquals("[T][ ] buy bread", new ToDo("buy bread").toString());
    }

    /** Verifies event fields and type formatting. */
    @Test
    void storesEventDetails() {
        Event event = new Event("project meeting", "Mon 2pm", "4pm");

        assertEquals("Mon 2pm", event.getFrom());
        assertEquals("4pm", event.getTo());
        assertEquals("[E][ ] project meeting (from: Mon 2pm to: 4pm)", event.toString());
    }
}
