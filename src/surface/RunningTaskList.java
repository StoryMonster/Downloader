package surface;

import javax.swing.JList;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.DefaultListCellRenderer;

import java.awt.Component;
import java.awt.Color;
import java.awt.GridLayout;

import network.DownloadStatus;
import services.DownloadService;

class TaskLabel {
    String taskName;
    String srcAddr;
    String localAddr;
    DownloadStatus status = DownloadStatus.analyzing;

    public TaskLabel(String taskName, String srcAddr, String localAddr)
    {
        this.taskName = taskName;
        this.srcAddr = srcAddr;
        this.localAddr = localAddr;
    }

    /*
    status: new status
    return: old status
    */
    public DownloadStatus changeStatus(DownloadStatus status)
    {
        DownloadStatus oldStatus = this.status;
        this.status = status;
        return oldStatus;
    }

    private String getStatusColor()
    {
        switch (status) {
            case downloading : return "Green";
            case analyzing: return "Yellow";
            case complete: return "Green";
            case fail: return "Red";
        }
        System.out.println("Unkown status");
        return "Yellow";
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
            "</html>", this.taskName, this.srcAddr, this.localAddr, statusColor, this.status);
    } 
}

public class RunningTaskList extends JList<TaskLabel> {
    DefaultListModel<TaskLabel> listModel = new DefaultListModel<TaskLabel>();
    DownloadService dlService = new DownloadService();

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
        if (dlService.addDownloadTask(taskName, remoteAddr, localAddr))
        {
            TaskLabel elem = new TaskLabel(taskName, remoteAddr, localAddr);
            listModel.addElement(elem);
            return true;
        }
        return false;
    }

    public void startDownload(String taskName)
    {
        updateTaskStatus(taskName);
        dlService.startDownload(taskName);
    }

    public void updateTaskStatus(String taskName)
    {
        for (int i = 0; i < listModel.getSize(); ++i)
        {
            TaskLabel elem = listModel.getElementAt(i);
            if (elem.taskName == taskName)
            {
                elem.changeStatus(DownloadStatus.downloading);
                return;
            }
        }
    }
}