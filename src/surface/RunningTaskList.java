package surface;

import javax.swing.JList;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.DefaultListCellRenderer;

import java.awt.Component;
import java.awt.Color;
import java.awt.GridLayout;

class TaskLabel {
    String taskName;
    String srcAddr;
    String localAddr;
    String status = "CREATING";

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
    public String changeStatus(String status)
    {
        String oldStatus = this.status;
        this.status = status;
        return oldStatus;
    }

    @Override
    public String toString() { 
        return String.format(
            "<html>" +
            "<h4>%s<br>" +
            "download from %s<br>" +
            "save to %s<br>" +
            "<h4>%s" +
            "</html>", this.taskName, this.srcAddr, this.localAddr, this.status);
    } 
}
public class RunningTaskList extends JList<TaskLabel> {
    DefaultListModel<TaskLabel> listModel = new DefaultListModel<TaskLabel>();

    public RunningTaskList() {
        setModel(listModel);
        setBorder(BorderFactory.createLineBorder(Color.black));

        // set cell style of list
        setFixedCellHeight(80);
        setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list,
                    Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                JLabel listCellRendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                listCellRendererComponent.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
                return listCellRendererComponent;
            }
        });
    }

    public void addRunningTask(String taskName, String remoteAddr, String localAddr)
    {
        TaskLabel elem = new TaskLabel(taskName, remoteAddr, localAddr);
        elem.changeStatus("DOWNLOADING");
        listModel.addElement(elem);
    }
}