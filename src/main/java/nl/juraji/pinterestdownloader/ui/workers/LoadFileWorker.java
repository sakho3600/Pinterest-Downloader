package nl.juraji.pinterestdownloader.ui.workers;

import nl.juraji.pinterestdownloader.configuration.AppData;
import nl.juraji.swing.JThrobber;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoadFileWorker extends SwingWorker<Void, Void> {
  private static final int TRUNCATE_TO = 200;

  private final File file;
  private final JTextComponent target;
  private final JThrobber throbber;
  private final JLabel fileInfoLabel;
  private final Document preloadDocument;
  private List<String> fileInfo;

  public LoadFileWorker(File file, JTextComponent target, JThrobber throbber, JLabel fileInfoLabel) {
    this.file = file;
    this.target = target;
    this.throbber = throbber;
    this.fileInfoLabel = fileInfoLabel;
    this.preloadDocument = new DefaultStyledDocument();
    this.fileInfo = new ArrayList<>();
  }

  @Override
  protected Void doInBackground() throws Exception {
    if (!file.exists()) {
      fileInfo.add("File does not exist");
    } else {
      if (throbber != null) throbber.setActive(true);
      List<String> lines = FileUtils.readLines(file, AppData.getCharset());
      if (lines.size() > TRUNCATE_TO) {
        lines = lines.subList(lines.size() - TRUNCATE_TO, lines.size() - 1);
        fileInfo.add("File was truncated to " + TRUNCATE_TO + " lines");
      }
      String truncated = lines.stream().reduce((sl, sr) -> sl += "\n" + sr).orElse("No Content.");
      preloadDocument.insertString(0, truncated, null);
      fileInfo.add("File Size: " + FileUtils.byteCountToDisplaySize(FileUtils.sizeOf(file)));
    }
    return null;
  }

  @Override
  protected void done() {
    target.setDocument(preloadDocument);
    if (fileInfoLabel != null) fileInfoLabel.setText(buildFileInfoString());
    if (throbber != null) throbber.setActive(false);
  }

  private String buildFileInfoString() {
    return fileInfo.stream().reduce((sl, sr) -> sl += ", " + sr).orElse("");
  }
}
