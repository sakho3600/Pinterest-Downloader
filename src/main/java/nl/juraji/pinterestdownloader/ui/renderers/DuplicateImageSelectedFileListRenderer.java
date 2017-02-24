package nl.juraji.pinterestdownloader.ui.renderers;

import nl.juraji.pinterestdownloader.configuration.AppIcons;
import nl.juraji.pinterestdownloader.utils.BasicJLabelHtml;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DuplicateImageSelectedFileListRenderer extends DefaultListCellRenderer {
  private static final int MAX_SIZE = 150;

  @Override
  public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    File file = new File((String) value);
    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    BasicJLabelHtml labelHtml = new BasicJLabelHtml();

    if (file.exists()) {
      BufferedImage image = fetchImage(file);
      Icon icon = createIcon(image);

      labelHtml.addLine(file.getName());
      labelHtml.addLine("Dimensions: " + image.getWidth() + " x " + image.getHeight());
      labelHtml.addLine("Size on disk: " + getFileSize(file));

      label.setIcon(icon);
      label.setText(labelHtml.build());
    } else {
      label.setIcon(AppIcons.getInstance().getIcon("missing_md"));
      label.setText(file.getName() + " (Deleted)");
      label.setForeground(Color.RED);
    }

    return label;
  }

  private BufferedImage fetchImage(File file) {
    try {
      return ImageIO.read(file);
    } catch (IOException ignored) {
    }

    return new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);
  }

  private String getFileSize(File file) {
    return FileUtils.byteCountToDisplaySize(FileUtils.sizeOf(file));
  }

  private Icon createIcon(BufferedImage image) {
    BufferedImage resultImage = new BufferedImage(MAX_SIZE, MAX_SIZE, BufferedImage.TYPE_INT_ARGB);

    double imgWidth = image.getWidth();
    double imgHeight = image.getHeight();
    double ratio = Math.min((MAX_SIZE / imgWidth), (MAX_SIZE / imgHeight));
    int tgtWidth = (int) (imgWidth * ratio);
    int tgtHeight = (int) (imgHeight * ratio);
    int xOffset = (MAX_SIZE - tgtWidth) / 2;
    int yOffset = (MAX_SIZE - tgtHeight) / 2;

    Graphics2D graphics = resultImage.createGraphics();
    graphics.drawImage(image, xOffset, yOffset, tgtWidth, tgtHeight, null);
    graphics.dispose();
    graphics.setComposite(AlphaComposite.Src);

    graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    return new ImageIcon(resultImage);
  }
}
