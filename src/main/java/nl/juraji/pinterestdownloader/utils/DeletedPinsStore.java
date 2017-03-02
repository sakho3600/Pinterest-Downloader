package nl.juraji.pinterestdownloader.utils;

import nl.juraji.pinterestdownloader.configuration.AppData;
import nl.juraji.pinterestdownloader.configuration.UIConfig;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class DeletedPinsStore {
  private static final String DELETED_PINS_TXT = "deletedPins.txt";
  private static DeletedPinsStore instance;
  private final File deletedPinsFile;

  private DeletedPinsStore() {
    deletedPinsFile = new File(UIConfig.getInstance().getDownloadTarget(), DELETED_PINS_TXT);
  }

  public static DeletedPinsStore getInstance() {
    if (instance == null) instance = new DeletedPinsStore();
    return instance;
  }

  public List<String> getDeletedPins() {
    List<String> deletedPins = new ArrayList<>();
    try {
      deletedPins.addAll(FileUtils.readLines(deletedPinsFile, AppData.getCharset()));
    } catch (IOException ignored) {
    }

    return deletedPins;
  }

  public void addDeletedPin(String deletedPinId) {
    try {
      String lSeparator = AppData.getNewlineSeparator();
      FileUtils.write(deletedPinsFile, deletedPinId + lSeparator, AppData.getCharset(), true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
