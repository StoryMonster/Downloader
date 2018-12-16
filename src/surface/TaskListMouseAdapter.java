package surface;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import surface.TaskList;

public class TaskListMouseAdapter extends MouseAdapter {
    TaskList taskLst;
    JPopupMenu popup;
    JMenuItem downloadItem;
    JMenuItem deleteItem;

    public TaskListMouseAdapter(TaskList taskLst)
    {
        this.taskLst = taskLst;
        
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
        taskLst.setSelectedIndex(taskLst.locationToIndex(event.getPoint()));
        DownloadTask task = taskLst.getSelectedValue();
        if (!task.isRemoteFileAccessable()) {
            downloadItem.setVisible(false);
        }
        popup.show(taskLst, event.getX(), event.getY());
    }

    private void onDownloadChoosed() {
        DownloadTask task = taskLst.getSelectedValue();
        System.out.println("download item is pressed on task " + task.getName());
        task.startDownload();
    }

    private void onDeleteChoosed() {
        DownloadTask task = taskLst.getSelectedValue();
        System.out.println("delele item is pressed on task " + task.getName());
    }
}