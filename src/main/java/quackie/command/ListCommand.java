package quackie.command;

import quackie.storage.Storage;
import quackie.task.TaskList;
import quackie.ui.Ui;

/**
 * Displays all tasks in the task list.
 */
public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTasks(tasks);
    }
}
