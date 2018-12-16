package surface;

import java.util.TimerTask;
import surface.TaskListModel;

public class TaskListModelPeriodicUpdateTask extends TimerTask {
    TaskListModel taskListModel;
    public TaskListModelPeriodicUpdateTask(TaskListModel model) {
        taskListModel = model;
    }

    @Override
    public void run() {
        taskListModel.updateAllElementsContent();
    }
}