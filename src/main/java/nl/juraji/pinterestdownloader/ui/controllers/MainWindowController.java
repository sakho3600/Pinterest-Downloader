package nl.juraji.pinterestdownloader.ui.controllers;

import nl.juraji.pinterestdownloader.configuration.AppData;
import nl.juraji.pinterestdownloader.configuration.AppIcons;
import nl.juraji.pinterestdownloader.ui.forms.MainWindowForm;
import nl.juraji.pinterestdownloader.ui.menus.MainMenu;

import javax.swing.*;

public final class MainWindowController extends JFrame {
  private static MainWindowController instance;

  private MainWindowController() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException ignored) {
    }

    MainWindowForm mainWindowForm = new MainWindowForm();
    MainMenu mainMenu = new MainMenu();

    setTitle(AppData.get("ApplicationName"));
    setContentPane(mainWindowForm.getMainPanel());
    setJMenuBar(mainMenu);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setIconImage(AppIcons.getInstance().getIcon("app_lg").getImage());
    pack();
    setVisible(true);
  }

  public static MainWindowController getInstance() {
    if (instance == null) instance = new MainWindowController();
    return instance;
  }
}
