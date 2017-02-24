package nl.juraji.pinterestdownloader.ui.menus;

import nl.juraji.pinterestdownloader.configuration.AppIcons;

import javax.swing.*;

public abstract class Menu extends JMenu {
  protected final AppIcons appIcons = AppIcons.getInstance();

  public Menu(String name) {
    super(name);
  }

  public void add(String title, Runnable listener, Icon icon) {
    JMenuItem menuItem = new JMenuItem(title);
    menuItem.addActionListener((actionEvent) -> listener.run());
    if (icon != null) menuItem.setIcon(icon);
    this.add(menuItem);
  }
}
