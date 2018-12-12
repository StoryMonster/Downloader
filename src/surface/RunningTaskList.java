package surface;

import javax.swing.JList;
import javax.swing.DefaultListModel;
import java.awt.GridLayout;


public class RunningTaskList extends JList<String> {
    DefaultListModel<String> listModel = new DefaultListModel<String>();

    public RunningTaskList() {
        setModel(listModel);
        setFixedCellHeight(50);
    }

    public void addElement(String elem)
    {
        listModel.addElement(elem);
    }
}