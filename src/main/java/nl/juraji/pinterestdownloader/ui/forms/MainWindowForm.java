package nl.juraji.pinterestdownloader.ui.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import nl.juraji.pinterestdownloader.configuration.AppIcons;
import nl.juraji.pinterestdownloader.configuration.UIConfig;
import nl.juraji.pinterestdownloader.ui.controllers.LockUIController;
import nl.juraji.pinterestdownloader.ui.controllers.ProgressController;
import nl.juraji.pinterestdownloader.ui.workers.ProcessWorker;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

public class MainWindowForm {
    private final UIConfig config = UIConfig.getInstance();
    private final ProcessWorker processWorker;

    private JPanel mainPanel;
    private JTextField targetDirectoryTextField;
    private JButton selectOtherDirectoryButton;
    private JCheckBox downloadBoardsCheckBox;
    private JCheckBox checkFileTypesCheckBox;
    private JButton startButton;
    private JProgressBar currentProcessProgressBar;
    private JTextField apiAccessKeyTextField;
    private JCheckBox openTargetOnDoneCheckBox;
    private JLabel currentProgressLabel;
    private JLabel downloadBoardsTaskLabel;
    private JLabel checkFileTypesTaskLabel;
    private JLabel openTargetOnDoneTaskLabel;
    private JCheckBox skipRecentlyUpdatedBoardsCheckBox;
    private JLabel skipRecentlyUpdatedLabel;

    public MainWindowForm() {
        apiAccessKeyTextField.setText(config.getApiAccessKey());
        targetDirectoryTextField.setText(config.getDownloadTarget().getAbsolutePath());
        downloadBoardsCheckBox.setSelected(config.isDoBoardDownload());
        skipRecentlyUpdatedBoardsCheckBox.setSelected(config.isDoSkipRecentBoards());
        checkFileTypesCheckBox.setSelected(config.isDoFileCheck());
        openTargetOnDoneCheckBox.setSelected(config.isOpenOnCompletion());

        updateTaskLabelIcon(downloadBoardsTaskLabel, config.isDoBoardDownload());
        updateTaskLabelIcon(skipRecentlyUpdatedLabel, config.isDoSkipRecentBoards());
        updateTaskLabelIcon(checkFileTypesTaskLabel, config.isDoFileCheck());
        updateTaskLabelIcon(openTargetOnDoneTaskLabel, config.isOpenOnCompletion());

        skipRecentlyUpdatedLabel.setEnabled(config.isDoBoardDownload());
        skipRecentlyUpdatedBoardsCheckBox.setEnabled(config.isDoBoardDownload());

        apiAccessKeyTextField.getDocument().addDocumentListener(this.onApiAccessKeyTextFieldChange());
        selectOtherDirectoryButton.addMouseListener(this.onSelectOtherDirectoryButtonClick());
        downloadBoardsCheckBox.addItemListener(this::onDownloadBoardsCheckBoxChange);
        skipRecentlyUpdatedBoardsCheckBox.addItemListener(this::onSkipRecentlyUpdatedBoardsCheckBoxChange);
        checkFileTypesCheckBox.addItemListener(this::onCheckFileTypesCheckBoxChange);
        openTargetOnDoneCheckBox.addItemListener(this::onOpenTargetOnDoneCheckBoxChange);
        startButton.addMouseListener(this.onStartButtonClick());

        ProgressController progressController = new ProgressController(currentProcessProgressBar, currentProgressLabel);

        LockUIController lockUIController = new LockUIController();
        lockUIController.addComponent(apiAccessKeyTextField);
        lockUIController.addComponent(selectOtherDirectoryButton);
        lockUIController.addComponent(downloadBoardsCheckBox);
        lockUIController.addComponent(checkFileTypesCheckBox);
        lockUIController.addComponent(openTargetOnDoneCheckBox);
        lockUIController.addComponent(startButton);

        processWorker = new ProcessWorker(progressController, lockUIController);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private DocumentListener onApiAccessKeyTextFieldChange() {
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                config.setApiAccessKey(apiAccessKeyTextField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                config.setApiAccessKey(apiAccessKeyTextField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                config.setApiAccessKey(apiAccessKeyTextField.getText());
            }
        };
    }

    private void onDownloadBoardsCheckBoxChange(ItemEvent itemEvent) {
        config.setDoBoardDownload(itemEvent.getStateChange() == ItemEvent.SELECTED);
        updateTaskLabelIcon(downloadBoardsTaskLabel, config.isDoBoardDownload());

        skipRecentlyUpdatedLabel.setEnabled(config.isDoBoardDownload());
        skipRecentlyUpdatedBoardsCheckBox.setEnabled(config.isDoBoardDownload());
    }

    private void onSkipRecentlyUpdatedBoardsCheckBoxChange(ItemEvent itemEvent) {
        config.setDoSkipRecentBoards(itemEvent.getStateChange() == ItemEvent.SELECTED);
        updateTaskLabelIcon(skipRecentlyUpdatedLabel, config.isDoSkipRecentBoards());
    }

    private void onCheckFileTypesCheckBoxChange(ItemEvent itemEvent) {
        config.setDoFileCheck(itemEvent.getStateChange() == ItemEvent.SELECTED);
        updateTaskLabelIcon(checkFileTypesTaskLabel, config.isDoFileCheck());
    }

    private void onOpenTargetOnDoneCheckBoxChange(ItemEvent itemEvent) {
        config.setOpenOnCompletion(itemEvent.getStateChange() == ItemEvent.SELECTED);
        updateTaskLabelIcon(openTargetOnDoneTaskLabel, config.isOpenOnCompletion());
    }

    private MouseListener onSelectOtherDirectoryButtonClick() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!e.getComponent().isEnabled()) return;
                JFileChooser fileChooser = new JFileChooser(config.getDownloadTarget());
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int choice = fileChooser.showDialog(e.getComponent(), null);
                if (choice == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    config.setDownloadTarget(selectedFile);
                    targetDirectoryTextField.setText(selectedFile.getAbsolutePath());
                }
            }
        };
    }

    private MouseAdapter onStartButtonClick() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!e.getComponent().isEnabled()) return;
                if (StringUtils.isBlank(apiAccessKeyTextField.getText())) {
                    showApiAccessKeyErrorDialog();
                    return;
                }

                processWorker.start();
            }
        };
    }

    private void showApiAccessKeyErrorDialog() {
        JOptionPane.showMessageDialog(
                null,
                "Please enter a valid Pinterest Api Access key",
                "Invalid Api Access key",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void updateTaskLabelIcon(JLabel label, boolean state) {
        AppIcons icons = AppIcons.getInstance();
        if (state) {
            label.setIcon(icons.getIcon("arrow_to"));
        } else {
            label.setIcon(icons.getIcon("arrow_over"));
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(9, 2, new Insets(5, 5, 5, 5), -1, -1));
        mainPanel.setInheritsPopupMenu(true);
        mainPanel.setMinimumSize(new Dimension(800, 360));
        mainPanel.setPreferredSize(new Dimension(800, 360));
        final JLabel label1 = new JLabel();
        label1.setText("Target Download Directory");
        mainPanel.add(label1, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currentProcessProgressBar = new JProgressBar();
        mainPanel.add(currentProcessProgressBar, new GridConstraints(8, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        targetDirectoryTextField = new JTextField();
        targetDirectoryTextField.setEditable(false);
        targetDirectoryTextField.setText("Target Directory");
        mainPanel.add(targetDirectoryTextField, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(659, 24), null, 0, false));
        selectOtherDirectoryButton = new JButton();
        selectOtherDirectoryButton.setText("Change...");
        mainPanel.add(selectOtherDirectoryButton, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currentProgressLabel = new JLabel();
        currentProgressLabel.setText("Waiting...");
        mainPanel.add(currentProgressLabel, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(6, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("What do you want me to do?");
        mainPanel.add(label2, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        startButton = new JButton();
        startButton.setText("Start");
        startButton.setVerticalAlignment(0);
        mainPanel.add(startButton, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 64), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Pinterest Api Access Token");
        mainPanel.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(659, 16), null, 0, false));
        apiAccessKeyTextField = new JTextField();
        mainPanel.add(apiAccessKeyTextField, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        downloadBoardsTaskLabel = new JLabel();
        downloadBoardsTaskLabel.setText("");
        panel1.add(downloadBoardsTaskLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(16, 16), new Dimension(16, 16), null, 0, false));
        downloadBoardsCheckBox = new JCheckBox();
        downloadBoardsCheckBox.setSelected(true);
        downloadBoardsCheckBox.setText("Download Boards");
        downloadBoardsCheckBox.setToolTipText("Download or update all boards for the current user");
        panel1.add(downloadBoardsCheckBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkFileTypesTaskLabel = new JLabel();
        checkFileTypesTaskLabel.setText("");
        panel1.add(checkFileTypesTaskLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(16, 16), new Dimension(16, 16), null, 0, false));
        checkFileTypesCheckBox = new JCheckBox();
        checkFileTypesCheckBox.setSelected(true);
        checkFileTypesCheckBox.setText("Scan Files Types");
        checkFileTypesCheckBox.setToolTipText("Scan the download directory for correct file extensions");
        panel1.add(checkFileTypesCheckBox, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        openTargetOnDoneTaskLabel = new JLabel();
        openTargetOnDoneTaskLabel.setText("");
        panel1.add(openTargetOnDoneTaskLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(16, 16), new Dimension(16, 16), null, 0, false));
        openTargetOnDoneCheckBox = new JCheckBox();
        openTargetOnDoneCheckBox.setSelected(true);
        openTargetOnDoneCheckBox.setText("Open Target Directory on Completion");
        panel1.add(openTargetOnDoneCheckBox, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        skipRecentlyUpdatedBoardsCheckBox = new JCheckBox();
        skipRecentlyUpdatedBoardsCheckBox.setText("Skip Recently Downloaded Boards");
        panel1.add(skipRecentlyUpdatedBoardsCheckBox, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        skipRecentlyUpdatedLabel = new JLabel();
        skipRecentlyUpdatedLabel.setText("");
        panel1.add(skipRecentlyUpdatedLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
