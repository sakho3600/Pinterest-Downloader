package nl.juraji.pinterestdownloader.ui.renderers;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class DuplicateImageFileListRenderer extends DefaultListCellRenderer {
  @Override
  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    return super.getListCellRendererComponent(list, formatRow(value), index, isSelected, cellHasFocus);
  }

  private String formatRow(Object value) {
    //noinspection unchecked
    List<String> files = (List<String>) value;
    String name = new File(files.get(0)).getName();
    return name.substring(0, 20) + " (" + files.size() + " pins)";
  }
}
