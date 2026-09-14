package quackie.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    /** Verifies that structured event times form a valid chronological range. */
    @Test
    void validatesStructuredEventTimes() {
        Event event = new Event("meeting", "2/12/2019 1400", "2/12/2019 1600");

        assertEquals("2/12/2019 1400", event.getFrom());
        assertThrows(IllegalArgumentException.class, () ->
                new Event("meeting", "2/12/2019 1600", "2/12/2019 1400"));
        assertThrows(IllegalArgumentException.class, () ->
                new Event("meeting", "2/12/2019 1400", "2/12/2019 1400"));
        assertThrows(IllegalArgumentException.class, () ->
                new Event("meeting", "2/12/2019 2500", "3/12/2019 1400"));
        assertThrows(IllegalArgumentException.class, () ->
                new Event("meeting", "2/12/2019 1400", "tomorrow"));
    }
}
