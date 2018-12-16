package surface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultListModel;

import surface.DownloadTask;

public class TaskListModel extends DefaultListModel<DownloadTask>{
    public void updateElementContent(String name)
    {
        for (int i = 0; i < getSize(); ++i)
        {
            if (getElementAt(i).getName().compareTo(name) == 0)
            {
                fireContentsChanged(this, i, i);
                System.out.println(String.format("content of element(index=%d) is changed", i));
                return ;
            }
        }
    }

    public void addTask(String taskName, String remoteAddr, String localAddr)
    {
        DownloadTask elem = new DownloadTask(taskName, remoteAddr, localAddr);
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
        return null;
    }

    public void deleteTask(String taskName)
    {
        for (int i = 0; i < getSize(); ++i)
        {
            DownloadTask task = getElementAt(i);
            if (taskName.compareTo(task.getName()) == 0)
            {
                removeElementAt(i);
            }
        }
    }
}