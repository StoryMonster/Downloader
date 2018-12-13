package surface;

import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.DefaultListCellRenderer;

import java.awt.Component;
import java.awt.Color;
import java.awt.GridLayout;

import network.DownloadStatusRef;
import network.FileDownloader;

class TaskLabel {
    String taskName;
    String srcAddr;
    String localAddr;
    DownloadStatusRef status;
    FileDownloader downloader;

    public TaskLabel(String taskName, String srcAddr, String localAddr)
    {
        this.taskName = taskName;
        this.srcAddr = srcAddr;
        this.localAddr = localAddr;
        this.status = new DownloadStatusRef();
        this.downloader = new FileDownloader(srcAddr, localAddr, this.status);
    }

    public String getName()
    {
        return taskName;
    }

    public void startDownload()
    {
        downloader.download();
    }

    private String getStatusColor()
    {
        switch (status.value) {
            case downloading : return "Green";
            case analyzing: return "Black";
            case complete: return "Green";
            case fail: return "Red";
        }
        System.out.println("Unkown status");
        return "Black";
    }

    @Override
    public String toString() {
        String statusColor = getStatusColor();
        return String.format(
            "<html>" +
            "<h4>%s<br>" +
            "download from %s<br>" +
            "save to %s<br>" +
            "<font color=%s>%s</font>" +
            "</html>", this.taskName, this.srcAddr, this.localAddr, statusColor, status.toString());
    }
}

class TaskListModel extends DefaultListModel<TaskLabel>{
    public void updateContext(String name)
    {
        fireContentsChanged(this, 0, 0);
    }
}

public class RunningTaskList extends JList<TaskLabel> {
    TaskListModel listModel = new TaskListModel();

    public RunningTaskList() {
        setModel(listModel);
        setBorder(BorderFactory.createLineBorder(Color.black));

        // set cell style of list
        setFixedCellHeight(80);
        setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list,
                    Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel listCellRendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                listCellRendererComponent.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
                return listCellRendererComponent;
            }
        });
    }

    public boolean addRunningTask(String taskName, String remoteAddr, String localAddr)
    {
        try
        {
            TaskLabel elem = new TaskLabel(taskName, remoteAddr, localAddr);
            listModel.addElement(elem);
            listModel.updateContext(taskName);
            return true;
        } catch(Exception e)
        {
            return false;
        }
    }

    private TaskLabel findTaskByName(String name)
    {
        for (int i = 0; i < listModel.getSize(); ++i)
        {
            TaskLabel task = listModel.getElementAt(i);
            if (name.compareTo(task.getName()) == 0)
            {
                return task;
            }
        }
        return null;
    }

    public void startDownload(String taskName)
    {
        TaskLabel task = findTaskByName(taskName);
        if (task == null)
        {
            System.out.println("Cannot find task " + taskName);
            return;
        }
        task.startDownload();
        listModel.updateContext(taskName);
    }
}