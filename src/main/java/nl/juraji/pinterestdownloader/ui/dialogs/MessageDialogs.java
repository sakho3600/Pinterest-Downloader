package nl.juraji.pinterestdownloader.ui.dialogs;

import nl.juraji.pinterestdownloader.configuration.AppIcons;

import javax.swing.*;

public final class MessageDialogs extends JOptionPane {
  private static final AppIcons APP_ICONS = AppIcons.getInstance();

  public static void showInfoMessageDialog(String title, String message) {
    showMessageDialog(getRootFrame(), message, title, INFORMATION_MESSAGE, APP_ICONS.getIcon("info"));
  }

  public static void showWarningConfirmDialog(String message, Runnable onOK) {
    int choice = showConfirmDialog(getRootFrame(), message, "Warning", YES_NO_OPTION, WARNING_MESSAGE, APP_ICONS.getIcon("warning"));
    if (choice == YES_OPTION) onOK.run();
  }

  public static void showErrorMessageDialog(String message) {
    showErrorMessageDialog("Error", message);
  }

  public static void showErrorMessageDialog(String title, String message) {
    showMessageDialog(getRootFrame(), message, title, ERROR_MESSAGE, APP_ICONS.getIcon("error"));
  }
}
