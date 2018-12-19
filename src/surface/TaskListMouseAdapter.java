package surface;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import download.DownloadStatus;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import surface.TaskList;
import log.*;


public class TaskListMouseAdapter extends MouseAdapter {
    TaskList taskList;
    JPopupMenu popup;
    JMenuItem downloadItem;
    JMenuItem deleteItem;

    public TaskListMouseAdapter(TaskList taskList)
    {
        this.taskList = taskList;
        
        downloadItem = new JMenuItem("download");
        downloadItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                onDownloadChoosed();
            }
        });

        deleteItem = new JMenuItem("delete");
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                onDeleteChoosed();
            }
        });

        popup = new JPopupMenu();
        popup.add(downloadItem);
        popup.add(deleteItem);
    }
    
    public void mousePressed(MouseEvent e)
    {
        if (e.isPopupTrigger())
        {
            showPopupMenu(e);
        }
    }

    public void mouseReleased(MouseEvent e)
    {
        if (e.isPopupTrigger())
        {
            showPopupMenu(e);
        }
    }

    private void showPopupMenu(MouseEvent event)
    {
        taskList.setSelectedIndex(taskList.locationToIndex(event.getPoint()));
        DownloadTask task = taskList.getSelectedValue();
        if (!task.isRemoteFileAccessable() || task.getStatus() == DownloadStatus.downloading) {
            downloadItem.setVisible(false);
        } else {
            downloadItem.setVisible(true);
        }
        popup.show(taskList, event.getX(), event.getY());
    }

    private void onDownloadChoosed() {
        DownloadTask task = taskList.getSelectedValue();
        LogInfo.log("Download task " + task.getName());
        if (task.getStatus() == DownloadStatus.complete)
        {
            String message = "re-download " + task.getName() + "?";
            JOptionPane.showMessageDialog(null, message);
        }
        task.startDownload();
    }

    private void onDeleteChoosed() {
        DownloadTask task = taskList.getSelectedValue();
        String taskName = task.getName();
        LogInfo.log("Delete task " + taskName);
        taskList.deleteTask(taskName);
    }
}