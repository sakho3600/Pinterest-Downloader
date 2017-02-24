package nl.juraji.pinterestdownloader.ui.renderers;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class DuplicateImageFileListRenderer extends DefaultListCellRenderer {
  @Override
  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    //noinspection unchecked
    List<String> files = (List<String>) value;
    return super.getListCellRendererComponent(list, new File(files.get(0)).getName(), index, isSelected, cellHasFocus);
  }
}
