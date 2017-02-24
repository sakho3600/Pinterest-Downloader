package nl.juraji.pinterestdownloader.ui.menus.submenus;

import nl.juraji.pinterestdownloader.configuration.UIConfig;
import nl.juraji.pinterestdownloader.ui.dialogs.WorkingDialog;
import nl.juraji.pinterestdownloader.ui.menus.Menu;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TargetDirectoryMenu extends Menu {
  private UIConfig config = UIConfig.getInstance();

  public TargetDirectoryMenu() {
    super("Target Directory");

    this.add("Open Target Directory", this::onOpenTargetDirectoryClick, appIcons.getIcon("view"));
    this.add("Clear Target Directory", this::onClearTargetDirectoryClick, appIcons.getIcon("folder_out"));
  }

  private void onOpenTargetDirectoryClick() {
    if (Desktop.isDesktopSupported()) try {
      Desktop.getDesktop().browse(config.getDownloadTarget().toURI());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void onClearTargetDirectoryClick() {
    String targetPath = config.getDownloadTarget().getAbsolutePath();

    int choice = JOptionPane.showConfirmDialog(
        getRootPane(),
        "Are you sure you want to delete the contents of " + targetPath,
        "Warning",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE
    );

    if (choice == JOptionPane.YES_OPTION) {
      WorkingDialog.showDialog(
          "Clearing " + targetPath + "...",
          () -> {
            try {
              FileUtils.deleteDirectory(config.getDownloadTarget());
              FileUtils.forceMkdir(config.getDownloadTarget());
            } catch (IOException ex) {
              JOptionPane.showMessageDialog(getRootPane(), "Clear target directory failed:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
          }
      );
    }
  }
}
