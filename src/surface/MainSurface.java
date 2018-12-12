package surface;

import javax.swing.*;

import javafx.scene.control.Dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Toolkit;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Vector;
import java.util.Map;

import services.DownloadService;
import surface.RunningTaskList;

public class MainSurface extends JFrame {
    String fileSaveDirectory = System.getProperties().getProperty("user.home");
    RunningTaskList lstDownloadingFiles = new RunningTaskList();
    JButton btnDlConfirm = new JButton("Download");
    JFileChooser fChooser = new JFileChooser(fileSaveDirectory);
    JLabel lblSaveTo = new JLabel("Save to " + fileSaveDirectory);
    JLabel lblDlFrom = new JLabel("Download from ");
    JLabel lblSaveAs = new JLabel("Save as ");
    JTextField txtDlFrom = new JTextField();
    JTextField txtSaveAs = new JTextField();
    DownloadService dlService = new DownloadService();


    public MainSurface(Dimension frameSize) {
        super("Downloader");
        setVisible(true);
        setPreferredSize(frameSize);
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width -  frameSize.width)/2, (screenSize.height - frameSize.height)/2);
        setLayout(new GridLayout(1, 2));

        JPanel dlCfgPanel = new JPanel();
        setDownloadConfigurationPanel(dlCfgPanel);

        JPanel dlDisplayPanel = new JPanel();
        setDownloadProcessDisplayPanel(dlDisplayPanel);

        add(dlCfgPanel);
        add(dlDisplayPanel);
        pack();
    }

    private void setDownloadProcessDisplayPanel(JPanel panel)
    {
        GridLayout layout = new GridLayout(1, 1);
        panel.setLayout(layout);
        panel.add(lstDownloadingFiles);
    }

    private void setDownloadConfigurationPanel(JPanel panel)
    {
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);

        GridBagConstraints constraintOfLblDlFrom = new GridBagConstraints();
        constraintOfLblDlFrom.gridx = 0;
        constraintOfLblDlFrom.gridy = 0;
        constraintOfLblDlFrom.anchor = GridBagConstraints.WEST;
        constraintOfLblDlFrom.fill = GridBagConstraints.NONE;

        GridBagConstraints constraintOfTxtDlFrom = new GridBagConstraints();
        constraintOfTxtDlFrom.gridx = GridBagConstraints.RELATIVE;
        constraintOfTxtDlFrom.gridy = 0;
        constraintOfTxtDlFrom.gridwidth = GridBagConstraints.REMAINDER;
        constraintOfTxtDlFrom.anchor = GridBagConstraints.WEST;
        constraintOfTxtDlFrom.fill = GridBagConstraints.HORIZONTAL;

        GridBagConstraints constraintOfLblSaveAs = new GridBagConstraints();
        constraintOfLblSaveAs.gridx = 0;
        constraintOfLblSaveAs.gridy = 3;
        constraintOfLblSaveAs.anchor = GridBagConstraints.WEST;
        constraintOfLblSaveAs.fill = GridBagConstraints.NONE;

        GridBagConstraints constraintOfTxtSaveAs = new GridBagConstraints();
        constraintOfTxtSaveAs.gridx = GridBagConstraints.RELATIVE;
        constraintOfTxtSaveAs.gridy = 3;
        constraintOfTxtSaveAs.gridwidth = GridBagConstraints.REMAINDER;
        constraintOfTxtSaveAs.anchor = GridBagConstraints.WEST;
        constraintOfTxtSaveAs.fill = GridBagConstraints.HORIZONTAL;

        GridBagConstraints constraintOfBtnDlConfirm = new GridBagConstraints();
        constraintOfBtnDlConfirm.gridx = 0;
        constraintOfBtnDlConfirm.gridy = 6;
        constraintOfBtnDlConfirm.gridwidth = GridBagConstraints.REMAINDER;
        constraintOfBtnDlConfirm.anchor = GridBagConstraints.CENTER;
        constraintOfBtnDlConfirm.fill = GridBagConstraints.HORIZONTAL;

        GridBagConstraints constraintOfLblSaveTo= new GridBagConstraints();
        constraintOfLblSaveTo.gridx = 0;
        constraintOfLblSaveTo.gridy = 9;
        constraintOfLblSaveTo.gridwidth = GridBagConstraints.REMAINDER;
        constraintOfLblSaveTo.anchor = GridBagConstraints.WEST;
        constraintOfLblSaveTo.fill = GridBagConstraints.HORIZONTAL;

        GridBagConstraints constraintOfFChooser= new GridBagConstraints();
        constraintOfFChooser.gridx = 0;
        constraintOfFChooser.gridy = GridBagConstraints.RELATIVE;
        constraintOfFChooser.gridwidth = GridBagConstraints.REMAINDER;
        constraintOfFChooser.gridheight = GridBagConstraints.REMAINDER;
        constraintOfFChooser.anchor = GridBagConstraints.NORTHWEST;
        constraintOfFChooser.fill = GridBagConstraints.BOTH;

        layout.setConstraints(lblDlFrom, constraintOfLblDlFrom);
        layout.setConstraints(txtDlFrom, constraintOfTxtDlFrom);
        layout.setConstraints(lblSaveAs, constraintOfLblSaveAs);
        layout.setConstraints(txtSaveAs, constraintOfTxtSaveAs);
        layout.setConstraints(btnDlConfirm, constraintOfBtnDlConfirm);
        layout.setConstraints(lblSaveTo, constraintOfLblSaveTo);
        layout.setConstraints(fChooser, constraintOfFChooser);

        fChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileSaveDirectory = fChooser.getSelectedFile().getPath();
                lblSaveTo.setText("Save to " + fileSaveDirectory);
            }
        });

        btnDlConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String remoteFileAddr = txtDlFrom.getText();
                String saveAs = txtSaveAs.getText();
                if (remoteFileAddr.length() == 0 || saveAs.length() == 0 || fileSaveDirectory.length() == 0 )
                {
                    JOptionPane.showMessageDialog(null, "Fill neccessary fields at first");
                    return;
                }
                if (dlService.addDownloadTask(remoteFileAddr, remoteFileAddr, fileSaveDirectory+"/"+saveAs))
                {
                    lstDownloadingFiles.addElement(remoteFileAddr);
                    dlService.startDownload(remoteFileAddr);
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Can't create task");
                }
            }
        });

        panel.add(lblDlFrom);
        panel.add(txtDlFrom);
        panel.add(lblSaveAs);
        panel.add(txtSaveAs);
        panel.add(btnDlConfirm);
        panel.add(lblSaveTo);
        panel.add(fChooser);
    }
}