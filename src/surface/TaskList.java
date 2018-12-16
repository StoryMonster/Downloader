package surface;

import javax.swing.JList;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.DefaultListCellRenderer;

import java.awt.Component;
import java.awt.Color;
import java.awt.GridLayout;

import surface.TaskListModel;
import surface.DownloadTask;
import surface.TaskListMouseAdapter;

public class TaskList extends JList<DownloadTask> {
    TaskListModel listModel = new TaskListModel();

    public TaskList() {
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
        addMouseListener(new TaskListMouseAdapter(this));
    }

    public boolean addRunningTask(String taskName, String remoteAddr, String localAddr)
    {
        listModel.addTask(taskName, remoteAddr, localAddr);
        return true;
    }

    public DownloadTask getTask(String taskName)
    {
        return listModel.getTask(taskName);
    }

    public void deleteTask(String taskName)
    {
        listModel.deleteTask(taskName);
    }

}