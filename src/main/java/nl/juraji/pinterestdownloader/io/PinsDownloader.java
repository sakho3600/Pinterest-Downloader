package nl.juraji.pinterestdownloader.io;

import nl.juraji.pinterestdownloader.configuration.AppData;
import nl.juraji.pinterestdownloader.model.pinterest.objects.Board;
import nl.juraji.pinterestdownloader.model.pinterest.objects.Pin;
import nl.juraji.pinterestdownloader.utils.DeletedPinsStore;
import nl.juraji.pinterestdownloader.utils.Logger;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PinsDownloader {
  private final static int[] ILLEGAL_CHARS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22,
      23, 24, 25, 26, 27, 28, 29, 30, 31, 34, 42, 47, 58, 60, 62, 63, 92, 124};

  private final List<String> existingPinIds;
  private final Runnable onTick;
  private final Logger logger;
  private final File boardDir;

  public PinsDownloader(Board board, File baseDownloadDir, Runnable onFileComplete) {
    this.onTick = onFileComplete;

    String safeName = toFileSystemSafeName(board.getName());
    boardDir = new File(baseDownloadDir, safeName);
    logger = new Logger(baseDownloadDir, "downloadLog.txt");
    existingPinIds = new ArrayList<>();
  }

  public File getBoardDir() {
    return boardDir;
  }

  public void start(List<Pin> pins) throws IOException {
    Files.createDirectories(boardDir.toPath());
    findExistingPins();
    pins.forEach(this::downloadAndSave);
  }

  private void findExistingPins() {
    Collection<File> files = FileUtils.listFiles(boardDir, AppData.getImgFileTypes(), false);
    existingPinIds.addAll(DeletedPinsStore.getInstance().getDeletedPins());
    files.stream()
        .map(file -> file.getName().replaceAll("^([0-9]+).*$", "$1"))
        .forEach(existingPinIds::add);
  }

  private void downloadAndSave(Pin pin) {
    if (!existingPinIds.contains(pin.getId())) {
      String safeNote = toFileSystemSafeName(pin.getNote());
      String pinImageUri = pin.getImage().getOriginal().getUrl();
      String imageFileType = pinImageUri.substring(pinImageUri.lastIndexOf("."));
      File pinFile = new File(boardDir, pin.getId() + "_" + safeNote + imageFileType);

      try {
        FileUtils.copyURLToFile(new URL(pinImageUri), pinFile);
        logger.log("Downloaded \"" + pinImageUri + "\" to \"" + pinFile.getAbsolutePath() + "\"");
      } catch (IOException e) {
        logger.log("Download failed for \"" + pinImageUri + "\" to \"" + pinFile.getAbsolutePath() + "\" (" + e.getMessage() + ")");
      }
    }

    onTick.run();
  }

  private static String toFileSystemSafeName(String name) {
    StringBuilder cleanName = new StringBuilder();
    int len = name.codePointCount(0, name.length());
    for (int i = 0; i < len; i++) {
      int c = name.codePointAt(i);
      if (Arrays.binarySearch(ILLEGAL_CHARS, c) < 0) {
        cleanName.appendCodePoint(c);
      } else {
        cleanName.append("_");
      }
    }
    String result = cleanName.toString();
    if (result.length() > 64) result = result.substring(0, 63);
    return result.trim();
  }
}
