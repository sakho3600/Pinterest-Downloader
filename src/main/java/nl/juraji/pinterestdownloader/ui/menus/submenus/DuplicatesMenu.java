package nl.juraji.pinterestdownloader.ui.menus.submenus;

import nl.juraji.pinterestdownloader.ui.dialogs.DuplicateFileScannerDialog;
import nl.juraji.pinterestdownloader.ui.menus.Menu;

public class DuplicatesMenu extends Menu{
  public DuplicatesMenu() {
    super("Duplicates");

    this.add("Open Duplicates Scanner", this::onOpenDuplicatesScannerClick, appIcons.getIcon("duplicates"));
  }

  private void onOpenDuplicatesScannerClick() {
    DuplicateFileScannerDialog.showDialog();
  }
}
