package nl.juraji.pinterestdownloader.ui.menus.submenus;

import nl.juraji.pinterestdownloader.configuration.UIConfig;
import nl.juraji.pinterestdownloader.ui.menus.Menu;
import nl.juraji.pinterestdownloader.ui.workers.ProbeApiLimitsWorker;

import javax.swing.*;

public class ApplicationMenu extends Menu {
  private final UIConfig config = UIConfig.getInstance();

  public ApplicationMenu() {
    super("Application");

    this.add("Probe Api Limits", this::onProbeApiLimitsClick, appIcons.getIcon("info"));
    this.addSeparator();
    this.add("Exit", this::onExitClick, appIcons.getIcon("exit"));
  }

  private void onProbeApiLimitsClick() {
    ProbeApiLimitsWorker apiLimitsWorker = new ProbeApiLimitsWorker(apiLimits ->
        JOptionPane.showMessageDialog(
            getRootPane(),
            "Limit for access token: " + apiLimits.getTotalLimit() + " per hour\n" +
                "Used calls: " + apiLimits.getCallCount() + "\n" +
                "Calls remaining: " + apiLimits.getRemainingCount() + "\n",
            "Api Limits",
            JOptionPane.INFORMATION_MESSAGE
        )
    );
    apiLimitsWorker.execute();
  }

  private void onExitClick() {
    System.exit(0);
  }
}
