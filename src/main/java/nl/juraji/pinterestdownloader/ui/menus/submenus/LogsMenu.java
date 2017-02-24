package nl.juraji.pinterestdownloader.ui.menus.submenus;

import nl.juraji.pinterestdownloader.configuration.UIConfig;
import nl.juraji.pinterestdownloader.ui.dialogs.LogFileDialog;
import nl.juraji.pinterestdownloader.ui.menus.Menu;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LogsMenu extends Menu {
  private final UIConfig config = UIConfig.getInstance();

  public LogsMenu() {
    super("Logs");

    getLogFiles().entrySet().forEach(log ->
        this.add("Open " + log.getValue(), () -> this.onOpenLogClick(log.getKey()), appIcons.getIcon("view")));
  }

  private void onOpenLogClick(String logFile) {
    LogFileDialog.showLog(new File(config.getDownloadTarget(), logFile));
  }

  private static Map<String, String> getLogFiles() {
    Map<String, String> logFilesMap = new HashMap<>();

    logFilesMap.put("downloadLog.txt", "Download Log");
    logFilesMap.put("fileTypeFixLog.txt", "File Scan Log");
    logFilesMap.put("deletedPins.txt", "Deleted Duplicate Pins");

    return logFilesMap;
  }
}
