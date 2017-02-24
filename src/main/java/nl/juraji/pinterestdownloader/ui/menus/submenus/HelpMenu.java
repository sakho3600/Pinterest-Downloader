package nl.juraji.pinterestdownloader.ui.menus.submenus;

import nl.juraji.pinterestdownloader.ui.menus.Menu;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HelpMenu extends Menu {

  public HelpMenu() {
    super("Help");

    this.add(new HelpPagesSubMenu());
    this.addSeparator();
    this.add("Juraji.nl", this::onJurajiNlClick, appIcons.getIcon("juraji"));
  }

  private void onJurajiNlClick() {
    if (Desktop.isDesktopSupported()) {
      try {
        Desktop.getDesktop().browse(new URI("https://juraji.nl"));
      } catch (IOException | URISyntaxException e) {
        e.printStackTrace();
      }
    }
  }
}
