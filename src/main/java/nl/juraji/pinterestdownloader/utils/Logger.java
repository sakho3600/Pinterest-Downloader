package nl.juraji.pinterestdownloader.utils;

import nl.juraji.pinterestdownloader.configuration.AppData;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Logger {
  private final File logFile;

  public Logger(File baseDir, String fileName) {
    logFile = new File(baseDir, fileName);
  }

  public void log(String message) {
    try {
      FileUtils.writeStringToFile(logFile, message + AppData.getNewlineSeparator(), AppData.getCharset(), true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
