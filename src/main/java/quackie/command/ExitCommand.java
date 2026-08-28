package quackie.command;

import quackie.storage.Storage;
import quackie.task.TaskList;
import quackie.ui.Ui;

/**
 * Ends the current Quackie session.
 */
public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showBye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
