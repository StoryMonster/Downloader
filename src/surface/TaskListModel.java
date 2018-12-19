package surface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultListModel;
import java.util.Timer;

import surface.DownloadTask;
import surface.TaskListModelPeriodicUpdateTask;
import log.*;

import java.lang.RuntimeException;

public class TaskListModel extends DefaultListModel<DownloadTask>{
    Timer timer = null;
    long contentUpdatePeriod = 1000;   // ms

    public TaskListModel() {
        timer = new Timer();
        timer.schedule(new TaskListModelPeriodicUpdateTask(this), 0, contentUpdatePeriod);
        LogDebug.log(String.format("Create timer for task list update, period=%d", contentUpdatePeriod));
    }

    public void updateElementContent(String name)
    {
        for (int i = 0; i < getSize(); ++i)
        {
            if (getElementAt(i).getName().compareTo(name) == 0)
            {
                fireContentsChanged(this, i, i);
                return ;
            }
        }
    }

    public void updateAllElementsContent()
    {
        for (int i = 0; i < getSize(); ++i)
        {
            getElementAt(i).updateContent();
        }
    }

    private boolean isTaskExist(String taskName) {
        for (int i = 0; i < getSize(); ++i)
        {
            if (getElementAt(i).getName().compareTo(taskName) == 0)
            {
                return true;
            }
        }
        return false;
    }

    public void addTask(String taskName, String remoteAddr, String localAddr)
    {
        if (isTaskExist(taskName))
        {
            throw (new RuntimeException(String.format("Task %s is already exist", taskName)));
        }
        DownloadTask elem = new DownloadTask(taskName, remoteAddr, localAddr);
        LogInfo.log(String.format("Task %s created", taskName));
        LogDebug.log(String.format("read from %s\nsave to %s", remoteAddr, localAddr));
        PropertyChangeListener listener = new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateElementContent(taskName);
            }
        };
        elem.addPropertyChangeListener("text", listener);
        addElement(elem);
    }

    public DownloadTask getTask(String taskName)
    {
        for (int i = 0; i < getSize(); ++i)
        {
            DownloadTask task = getElementAt(i);
            if (taskName.compareTo(task.getName()) == 0)
            {
                return task;
            }
        }
        LogWarn.log("Cannot find task " + taskName);
        return null;
    }

    public void deleteTask(String taskName)
    {
        for (int i = 0; i < getSize(); ++i)
        {
            DownloadTask task = getElementAt(i);
            if (taskName.compareTo(task.getName()) == 0)
            {
                task.stopDownload();
                removeElementAt(i);
                return ;
            }
        }
        LogWarn.log("Cannot find task " + taskName);
    }
}