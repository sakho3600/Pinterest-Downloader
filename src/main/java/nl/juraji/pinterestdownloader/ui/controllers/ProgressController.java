package nl.juraji.pinterestdownloader.ui.controllers;

import javax.swing.*;

public class ProgressController {
  private final JProgressBar progressBar;
  private final JLabel progressLabel;

  private String currentProcessName;

  public ProgressController(JProgressBar progressBar, JLabel progressLabel) {
    this.progressBar = progressBar;
    this.progressLabel = progressLabel;
    this.progressBar.setMinimum(0);

    pushNewProcess("Set options and click Start");
  }

  public void pushNewProcess(String processName) {
    pushNewProcess(processName, 0);
  }

  public void pushNewProcess(String processName, int itemCount) {
    SwingUtilities.invokeLater(() -> {
      progressBar.setMaximum(itemCount);
      progressBar.setValue(0);
      currentProcessName = processName;
      updateProgressLabel(0, itemCount);
    });
  }

  public void doTick() {
    SwingUtilities.invokeLater(() -> {
      int currentValue = progressBar.getValue() + 1;
      if (currentValue > progressBar.getMaximum()) return;
      progressBar.setValue(currentValue);
      updateProgressLabel(currentValue, progressBar.getMaximum());
    });
  }

  private void updateProgressLabel(int current, int itemCount) {
    if (current == 0 && itemCount == 0) {
      progressLabel.setText(currentProcessName);
    } else {
      progressLabel.setText(currentProcessName + " " + current + "/" + itemCount);
    }
  }
}
