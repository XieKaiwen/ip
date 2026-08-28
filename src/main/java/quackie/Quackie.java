package quackie;

import java.io.IOException;

import quackie.command.Command;
import quackie.parser.Parser;
import quackie.storage.Storage;
import quackie.task.TaskList;
import quackie.ui.Ui;

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
                try {
                    Command parsedCommand = parser.parse(command, tasks);
                    parsedCommand.execute(tasks, ui, storage);
                    if (parsedCommand.isExit()) {
                        return;
                    }
                } catch (IllegalArgumentException exception) {
                    ui.showError(exception.getMessage());
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

}
