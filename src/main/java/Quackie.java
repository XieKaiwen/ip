import java.io.IOException;

/**
 * Entry point for the Quackie chatbot.
 */
public class Quackie {
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();
        TaskList tasks;
        Storage storage = new Storage();
        Parser parser = new Parser();
        tasks = loadTasks(storage, ui);

        while (true) {
            String command = ui.readCommand();
            if (command == null) {
                return;
            }
            ui.showLine();

            if (command.isBlank()) {
                ui.showError("Please enter a command.");
            } else {
                switch (parser.parseCommandType(command)) {
                case BYE:
                    ui.showBye();
                    return;
                case LIST:
                    ui.showTasks(tasks);
                    break;
                case DELETE:
                    int deleteIndex = parser.parseTaskIndex(command, "delete", tasks);
                    if (deleteIndex >= 0) {
                        Task removedTask = tasks.delete(deleteIndex);
                        saveTasks(storage, tasks, ui);
                        ui.showTaskDeleted(removedTask, tasks.size());
                    } else {
                        ui.showError("Please provide a valid task number.");
                    }
                    break;
                case MARK:
                    int markIndex = parser.parseTaskIndex(command, "mark", tasks);
                    if (markIndex >= 0) {
                        tasks.markAsDone(markIndex);
                        saveTasks(storage, tasks, ui);
                        ui.showTaskMarked(tasks.get(markIndex));
                    } else {
                        ui.showError("Please provide a valid task number.");
                    }
                    break;
                case UNMARK:
                    int unmarkIndex = parser.parseTaskIndex(command, "unmark", tasks);
                    if (unmarkIndex >= 0) {
                        tasks.markAsUndone(unmarkIndex);
                        saveTasks(storage, tasks, ui);
                        ui.showTaskUnmarked(tasks.get(unmarkIndex));
                    } else {
                        ui.showError("Please provide a valid task number.");
                    }
                    break;
                case EVENT:
                case DEADLINE:
                case TODO:
                    try {
                        Task task = parser.parseTask(command);
                        tasks.add(task);
                        saveTasks(storage, tasks, ui);
                        ui.showTaskAdded(task, tasks.size());
                    } catch (IllegalArgumentException exception) {
                        ui.showError(exception.getMessage());
                    }
                    break;
                case UNKNOWN:
                    ui.showError("I don't recognize that command.");
                    break;
                }
            }

            ui.showLine();
        }
    }

    /**
     * Loads saved tasks while keeping the chatbot usable when the data is invalid.
     *
     * @param storage the storage service to read from
     * @return the loaded task list, or an empty list when loading fails
     */
    private static TaskList loadTasks(Storage storage, Ui ui) {
        TaskList tasks = new TaskList();
        try {
            storage.load(tasks);
            return tasks;
        } catch (IOException | RuntimeException exception) {
            ui.showError("I couldn't load saved tasks. Starting with an empty list.");
            return new TaskList();
        }
    }

    /**
     * Saves tasks while keeping the chatbot usable when the data file is unavailable.
     *
     * @param storage the storage service to write to
     * @param tasks the task list to save
     */
    private static void saveTasks(Storage storage, TaskList tasks, Ui ui) {
        try {
            storage.save(tasks);
        } catch (IOException exception) {
            ui.showError("I couldn't save your tasks.");
        }
    }

}
