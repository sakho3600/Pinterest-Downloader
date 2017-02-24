package nl.juraji.pinterestdownloader.ui.workers;

import nl.juraji.images.duplicates.DuplicateImageFinder;
import nl.juraji.pinterestdownloader.configuration.AppData;
import nl.juraji.pinterestdownloader.configuration.UIConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import javax.swing.*;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

public class DuplicateImageScannerWorker extends SwingWorker<Void, List<String>> {
  private final UIConfig config = UIConfig.getInstance();
  private final DefaultListModel<List<String>> listModel;
  private final BiConsumer<String, Integer> onNextBoard;
  private final Runnable onStart;
  private final Runnable onDone;

  public DuplicateImageScannerWorker(DefaultListModel<List<String>> listModel, BiConsumer<String, Integer> onNextBoard, Runnable onStart, Runnable onDone) {
    this.listModel = listModel;
    this.onNextBoard = onNextBoard;
    this.onStart = onStart;
    this.onDone = onDone;
    this.listModel.clear();
  }

  @Override
  protected Void doInBackground() throws Exception {
    onStart.run();
    File[] directories = config.getDownloadTarget().listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);

    if (directories == null) {
      return null;
    }

    Collection<File> boardDirectories = Arrays.asList(directories);

    boardDirectories.forEach(boardDirectory -> {
      if (isCancelled()) return;
      Collection<File> files = FileUtils.listFiles(boardDirectory, AppData.getImgFileTypes(), false);
      onNextBoard.accept(boardDirectory.getName(), files.size());
      List<List<String>> duplicates = DuplicateImageFinder.findAndGroupDuplicates(files);
      duplicates.forEach(this::publish);
    });

    onDone.run();
    return null;
  }

  @Override
  protected void process(List<List<String>> chunks) {
    chunks.forEach(listModel::addElement);
  }
}
