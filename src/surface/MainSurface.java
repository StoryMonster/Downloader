package surface;

import javax.swing.*;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import surface.TaskList;
import utils.FileHelper;
import context.MainSurfaceContext;
import log.*;

public class MainSurface extends JFrame {
    TaskList lstDownloadingFiles = new TaskList();
    JButton btnDlConfirm = new JButton("Download");
    JLabel lblDlFrom = new JLabel("Download from ");
    JLabel lblSaveAs = new JLabel("Save as ");
    JTextField txtSaveAs = new JTextField();
    MainSurfaceContext context = null;
    JFileChooser fChooser = null;
    JLabel lblSaveTo = null;
    JTextField txtDlFrom = null;

    public MainSurface(MainSurfaceContext context) {
        super("Downloader");
        this.context = context;
        context.localSaveDir = FileHelper.isDir(context.localSaveDir) ? context.localSaveDir : ".";
        this.fChooser = new JFileChooser(context.localSaveDir);
        this.lblSaveTo = new JLabel("Save to " + context.localSaveDir);
        this.txtDlFrom = new JTextField();

        initSurface();
    }

    private void initSurface()
    {
        setVisible(true);
        setPreferredSize(new Dimension(context.width, context.height));
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width -  context.width)/2, (screenSize.height - context.height)/2);
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
                context.localSaveDir = fChooser.getSelectedFile().getPath();
                lblSaveTo.setText("Save to " + context.localSaveDir);
            }
        });

        txtDlFrom.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                txtSaveAs.setText(FileHelper.getFileNameInUrl(txtDlFrom.getText()));
            }
            public void removeUpdate(DocumentEvent e) {
                txtSaveAs.setText(FileHelper.getFileNameInUrl(txtDlFrom.getText()));
            }
            public void insertUpdate(DocumentEvent e) {
                txtSaveAs.setText(FileHelper.getFileNameInUrl(txtDlFrom.getText()));
            }
        });

        btnDlConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String remoteFileAddr = txtDlFrom.getText();
                if (remoteFileAddr.length() == 0 || context.localSaveDir.length() == 0 )
                {
                    JOptionPane.showMessageDialog(null, "Fill neccessary fields at first");
                    return;
                }
                String fileNameToSave = txtSaveAs.getText();
                fileNameToSave = fileNameToSave.length() != 0 ? fileNameToSave : FileHelper.getFileNameInUrl("");
                String savedFilePath = context.localSaveDir+"/"+fileNameToSave;
                String taskName = String.copyValueOf(fileNameToSave.toCharArray());
                try {
                    lstDownloadingFiles.addRunningTask(taskName, remoteFileAddr, savedFilePath);
                    lstDownloadingFiles.getTask(taskName).analyzeRemoteFile();
                } catch(Exception ex)
                {
                    LogError.log(ex.toString());
                    JOptionPane.showMessageDialog(null, ex.toString());
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