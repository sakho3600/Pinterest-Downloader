package nl.juraji.pinterestdownloader.ui.menus;

import nl.juraji.pinterestdownloader.ui.menus.submenus.*;

import javax.swing.*;

public class MainMenu extends JMenuBar {
  public MainMenu() {
    this.add(new ApplicationMenu());
    this.add(new TargetDirectoryMenu());
    this.add(new LogsMenu());
    this.add(new DuplicatesMenu());
    this.add(new HelpMenu());
  }
}
