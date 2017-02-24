package nl.juraji.pinterestdownloader.ui.dialogs;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import nl.juraji.images.duplicates.util.HasherSettings;
import nl.juraji.pinterestdownloader.configuration.AppIcons;
import nl.juraji.pinterestdownloader.ui.controllers.MainWindowController;
import nl.juraji.pinterestdownloader.ui.renderers.DuplicateImageFileListRenderer;
import nl.juraji.pinterestdownloader.ui.renderers.DuplicateImageSelectedFileListRenderer;
import nl.juraji.pinterestdownloader.ui.workers.DuplicateImageScannerWorker;
import nl.juraji.pinterestdownloader.utils.DeletedPinsStore;
import nl.juraji.swing.JThrobber;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class DuplicateFileScannerDialog extends JDialog {
  private final DefaultListModel<List<String>> filesListModel;
  private final DefaultListModel<String> selectedFileListModel;
  private JPanel contentPane;
  private JButton doneButton;
  private JList<List<String>> fileList;
  private JButton startScanButton;
  private JThrobber scanActiveThrobber;
  private JSlider similarityInput;
  private JLabel similarityLabel;
  private JList<String> selectedFileList;
  private JButton deleteSelectedFilesButton;
  private JLabel currentBoardLabel;
  private JButton stopScanButton;
  private JCheckBox multithreadedScanningCheckBox;

  private DuplicateImageScannerWorker imageScannerWorker;

  public DuplicateFileScannerDialog() {
    setIconImage(AppIcons.getInstance().getIcon("duplicates").getImage());
    setTitle("Duplicates Search");
    setContentPane(contentPane);
    setModal(true);
    getRootPane().setDefaultButton(doneButton);
    setLocationRelativeTo(MainWindowController.getInstance().getContentPane());

    doneButton.addActionListener(e -> onDone());
    startScanButton.addActionListener(e -> onStartScanButtonClick());
    stopScanButton.addActionListener(e -> onStopScanButtonClick());

    // call onDone() when cross is clicked
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onDone();
      }
    });

    scanActiveThrobber.setActive(false);

    similarityInput.setMinimum(0);
    similarityInput.setMaximum(100);
    similarityInput.setValue(HasherSettings.getGoalSimilarityPercentage());
    similarityLabel.setText("Similarity: " + similarityInput.getValue() + "%");
    similarityInput.addChangeListener(e -> {
      similarityLabel.setText("Similarity: " + similarityInput.getValue() + "%");
      HasherSettings.setGoalSimilarityPercentage(similarityInput.getValue());
    });

    multithreadedScanningCheckBox.setSelected(HasherSettings.isMultiThreadingEnabled());
    multithreadedScanningCheckBox.addItemListener(this::onMultithreadedScanningCheckBoxChange);

    filesListModel = new DefaultListModel<>();
    fileList.setModel(filesListModel);
    fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    fileList.setCellRenderer(new DuplicateImageFileListRenderer());
    fileList.addMouseListener(this.onFileListClick());

    selectedFileListModel = new DefaultListModel<>();
    selectedFileList.setModel(selectedFileListModel);
    selectedFileList.setCellRenderer(new DuplicateImageSelectedFileListRenderer());
    selectedFileList.addMouseListener(this.onSelectedFileListDblClick());

    deleteSelectedFilesButton.addMouseListener(this.onDeleteSelectedFilesButtonClick());
  }

  private void onMultithreadedScanningCheckBoxChange(ItemEvent itemEvent) {
    HasherSettings.setMultiThreadingEnabled(itemEvent.getStateChange() == 1);
  }

  private void onDone() {
    if (imageScannerWorker != null && !imageScannerWorker.isDone() && !imageScannerWorker.isCancelled()) {
      imageScannerWorker.cancel(true);
    }

    dispose();
  }

  private void onStopScanButtonClick() {
    if (!stopScanButton.isEnabled()) return;
    if (imageScannerWorker != null && !imageScannerWorker.isDone() && !imageScannerWorker.isCancelled()) {
      imageScannerWorker.cancel(true);
    }
  }

  private void onStartScanButtonClick() {
    if (!startScanButton.isEnabled()) return;
    imageScannerWorker = new DuplicateImageScannerWorker(
        filesListModel,
        (boardName, imgCount) -> currentBoardLabel.setText("Now Scanning " + boardName + " (" + imgCount + " items)"),
        () -> {
          multithreadedScanningCheckBox.setEnabled(false);
          scanActiveThrobber.setActive(true);
          similarityInput.setEnabled(false);
          startScanButton.setEnabled(false);
          stopScanButton.setEnabled(true);
        },
        () -> {
          multithreadedScanningCheckBox.setEnabled(true);
          scanActiveThrobber.setActive(false);
          similarityInput.setEnabled(true);
          startScanButton.setEnabled(true);
          stopScanButton.setEnabled(false);
          currentBoardLabel.setText("");
        }
    );
    imageScannerWorker.execute();
  }

  private MouseAdapter onSelectedFileListDblClick() {
    return new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && Desktop.isDesktopSupported()) {
          try {
            JList list = (JList) e.getSource();
            list.setSelectedIndex(list.locationToIndex(e.getPoint()));
            String filePath = (String) list.getSelectedValue();
            Desktop.getDesktop().open(new File(filePath));
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        }
      }
    };
  }

  private MouseAdapter onDeleteSelectedFilesButtonClick() {
    return new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (!((JComponent) e.getSource()).isEnabled()) return;

        List<String> selectedValues = selectedFileList.getSelectedValuesList();
        if (selectedValues.isEmpty()) return;

        int choice = JOptionPane.showConfirmDialog(
            getRootPane(),
            "Are you sure you want to delete these " + selectedValues.size() + " Pins?",
            "Delete Duplicates",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
          selectedValues.forEach(pathname -> {
            try {
              FileUtils.forceDelete(new File(pathname));
            } catch (IOException e1) {
              e1.printStackTrace();
            }
          });

          DeletedPinsStore.getInstance().addDeletedPinsByFilename(selectedValues);

          selectedFileList.repaint();
        }
      }
    };
  }

  private MouseAdapter onFileListClick() {
    return new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        JList list = (JList) e.getSource();
        if (e.getClickCount() == 1) {
          selectedFileListModel.clear();
          List<String> files = filesListModel.get(list.locationToIndex(e.getPoint()));
          files.forEach(selectedFileListModel::addElement);
        }
      }
    };
  }

  public static void showDialog() {
    DuplicateFileScannerDialog dialog = new DuplicateFileScannerDialog();
    dialog.pack();
    dialog.setVisible(true);
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
    contentPane = new JPanel();
    contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
    contentPane.setMinimumSize(new Dimension(1000, 600));
    contentPane.setPreferredSize(new Dimension(1000, 600));
    contentPane.setRequestFocusEnabled(true);
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
    contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
    final JPanel panel2 = new JPanel();
    panel2.setLayout(new GridLayoutManager(1, 6, new Insets(0, 0, 0, 0), -1, -1));
    panel1.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    doneButton = new JButton();
    doneButton.setText("I'm Done!");
    panel2.add(doneButton, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final Spacer spacer1 = new Spacer();
    panel2.add(spacer1, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    startScanButton = new JButton();
    startScanButton.setBorderPainted(false);
    startScanButton.setText("Start Scan");
    panel2.add(startScanButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    stopScanButton = new JButton();
    stopScanButton.setEnabled(false);
    stopScanButton.setText("Stop Scan");
    panel2.add(stopScanButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    scanActiveThrobber = new JThrobber();
    panel2.add(scanActiveThrobber, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    currentBoardLabel = new JLabel();
    currentBoardLabel.setText("");
    panel2.add(currentBoardLabel, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel3 = new JPanel();
    panel3.setLayout(new GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
    panel1.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    deleteSelectedFilesButton = new JButton();
    deleteSelectedFilesButton.setText("Delete Selected");
    panel3.add(deleteSelectedFilesButton, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final Spacer spacer2 = new Spacer();
    panel3.add(spacer2, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    similarityInput = new JSlider();
    panel3.add(similarityInput, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), new Dimension(100, -1), new Dimension(100, -1), 0, false));
    similarityLabel = new JLabel();
    similarityLabel.setText("");
    panel3.add(similarityLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    multithreadedScanningCheckBox = new JCheckBox();
    multithreadedScanningCheckBox.setSelected(true);
    multithreadedScanningCheckBox.setText("Multithreaded Scanning");
    panel3.add(multithreadedScanningCheckBox, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JSplitPane splitPane1 = new JSplitPane();
    splitPane1.setDividerLocation(300);
    contentPane.add(splitPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
    final JScrollPane scrollPane1 = new JScrollPane();
    splitPane1.setLeftComponent(scrollPane1);
    fileList = new JList();
    scrollPane1.setViewportView(fileList);
    final JScrollPane scrollPane2 = new JScrollPane();
    splitPane1.setRightComponent(scrollPane2);
    selectedFileList = new JList();
    scrollPane2.setViewportView(selectedFileList);
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return contentPane;
  }
}
