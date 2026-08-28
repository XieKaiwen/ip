package quackie.command;

import quackie.storage.Storage;
import quackie.task.TaskList;
import quackie.ui.Ui;

/**
 * Reports a command that Quackie does not recognize.
 */
public class UnknownCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showError("I don't recognize that command.");
    }
}
