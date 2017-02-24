package nl.juraji.pinterestdownloader.io;

import net.sf.jmimemagic.*;
import nl.juraji.pinterestdownloader.configuration.AppData;
import nl.juraji.pinterestdownloader.utils.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class FileTypeFixer {
  private final Collection<File> filesToScan;
  private final Runnable onFileChecked;
  private final Logger logger;

  public FileTypeFixer(File basdir, Runnable onFileChecked) {
    logger = new Logger(basdir, "fileTypeFixLog.txt");
    filesToScan = FileUtils.listFiles(basdir, AppData.getImgFileTypes(), true);
    this.onFileChecked = onFileChecked;
  }

  public int getFileCount() {
    return filesToScan.size();
  }

  public void startScan() {
    filesToScan.forEach(this::processFile);
  }

  private void processFile(File originalfile) {
    try {
      File realExtFile = getFileWithRealType(originalfile);
      if (!originalfile.equals(realExtFile)) {
        FileUtils.moveFile(originalfile, realExtFile);
        logger.log("Moved \"" + originalfile.getName() + "\" to \"" + realExtFile.getName() + "\"");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    onFileChecked.run();
  }

  private File getFileWithRealType(File file) {
    try {
      MagicMatch magicMatch = Magic.getMagicMatch(file, false, true);

      if (!StringUtils.isEmpty(magicMatch.getExtension())) {
        String absPath = file.getAbsolutePath();
        return new File(absPath.substring(0, absPath.lastIndexOf(".") + 1) + magicMatch.getExtension());
      }
    } catch (MagicParseException | MagicMatchNotFoundException | MagicException ignored) {
    }

    return file;
  }
}
