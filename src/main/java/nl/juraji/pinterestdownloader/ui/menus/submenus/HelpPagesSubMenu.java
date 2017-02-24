package nl.juraji.pinterestdownloader.ui.menus.submenus;

import nl.juraji.pinterestdownloader.ui.dialogs.HelpDialog;
import nl.juraji.pinterestdownloader.ui.menus.Menu;

import java.util.LinkedHashMap;

public class HelpPagesSubMenu extends Menu {
  private static final String HELP_RESOURCE_BASE = "/nl/juraji/pinterestdownloader/resources/help/";

  public HelpPagesSubMenu() {
    super("Pages");

    this.setIcon(appIcons.getIcon("help"));

    getHelpPages().entrySet().forEach(pageEntry ->
        this.add(pageEntry.getValue(), () -> this.openHelpDialog(pageEntry.getKey()), appIcons.getIcon("help")));
  }

  private void openHelpDialog(String pageName) {
    HelpDialog.showHelp(HelpMenu.class.getResource(HELP_RESOURCE_BASE + pageName + ".html"));
  }

  private static LinkedHashMap<String, String> getHelpPages() {
    LinkedHashMap<String, String> pages = new LinkedHashMap<>();

    pages.put("download-pins", "Downloading Pins");
    pages.put("duplicate-scanner", "The Duplicates Scanner");

    return pages;
  }
}
