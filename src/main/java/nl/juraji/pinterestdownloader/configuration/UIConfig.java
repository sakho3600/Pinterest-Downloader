package nl.juraji.pinterestdownloader.configuration;

import java.io.*;
import java.util.Properties;

public final class UIConfig {
  private static final File CONFIG_STATE_FILE = new File(System.getProperty("user.dir"), "PinterestDownloader.properties");
  private static final String API_ACCESS_KEY = "ApiAccessKey";
  private static final String DOWNLOAD_TARGET = "DownloadTarget";
  private static final String DO_BOARD_DOWNLOAD = "DoBoardDownload";
  private static final String DO_SKIP_RECENT_BOARDS = "DoSkipRecentBoards";
  private static final String DO_FILE_CHECK = "DoFileCheck";
  private static final String OPEN_ON_COMPLETION = "OpenOnCompletion";

  private static UIConfig instance;
  private final Properties config;

  private UIConfig() {
    config = new Properties();
    load();
  }

  public static UIConfig getInstance() {
    if (instance == null) instance = new UIConfig();
    return instance;
  }

  public String getApiAccessKey() {
    return get(API_ACCESS_KEY);
  }

  public void setApiAccessKey(String apiAccessKey) {
    set(API_ACCESS_KEY, apiAccessKey);
  }

  public File getDownloadTarget() {
    return new File(get(DOWNLOAD_TARGET));
  }

  public void setDownloadTarget(File downloadTarget) {
    set(DOWNLOAD_TARGET, downloadTarget.getAbsolutePath());
  }

  public boolean isDoBoardDownload() {
    return "true".equals(get(DO_BOARD_DOWNLOAD));
  }

  public void setDoBoardDownload(boolean doBoardDownload) {
    set(DO_BOARD_DOWNLOAD, String.valueOf(doBoardDownload));
  }

  public boolean isDoFileCheck() {
    return "true".equals(get(DO_FILE_CHECK));
  }

  public void setDoFileCheck(boolean doFileCheck) {
    set(DO_FILE_CHECK, String.valueOf(doFileCheck));
  }

  public boolean isOpenOnCompletion() {
    return "true".equals(get(OPEN_ON_COMPLETION));
  }

  public void setOpenOnCompletion(boolean openOnCompletion) {
    set(OPEN_ON_COMPLETION, String.valueOf(openOnCompletion));
  }

  public boolean isDoSkipRecentBoards() {
    return "true".equals(get(DO_SKIP_RECENT_BOARDS));
  }

  public void setDoSkipRecentBoards(boolean doSkipRecentBoards) {
    set(DO_SKIP_RECENT_BOARDS, String.valueOf(doSkipRecentBoards));
  }

  private String get(String key) {
    if (config.containsKey(key)) {
      return config.getProperty(key);
    }
    return "";
  }

  private void set(String key, String value) {
    config.setProperty(key, value);
    save();
  }

  private void load() {
    try {
      if (CONFIG_STATE_FILE.exists()) {
        FileInputStream fileInputStream = new FileInputStream(CONFIG_STATE_FILE);
        config.load(fileInputStream);
      } else {
        setDownloadTarget(new File(System.getProperty("user.home")));
        setDoBoardDownload(true);
        setDoSkipRecentBoards(true);
        setDoFileCheck(true);
        setOpenOnCompletion(true);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void save() {
    try {
      boolean fileExists = CONFIG_STATE_FILE.exists();
      if (!fileExists) fileExists = CONFIG_STATE_FILE.createNewFile();
      if (!fileExists) throw new IOException("Could not create app state file");
      FileOutputStream fileOutputStream = new FileOutputStream(CONFIG_STATE_FILE);
      config.store(fileOutputStream, "PinterestDownloader Application state");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
