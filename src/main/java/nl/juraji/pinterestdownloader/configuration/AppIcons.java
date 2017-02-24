package nl.juraji.pinterestdownloader.configuration;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AppIcons {
  private static AppIcons instance;
  private final Map<String, ImageIcon> iconMap;

  private AppIcons() {
    iconMap = new HashMap<>();

    Toolkit toolkit = Toolkit.getDefaultToolkit();
    List<String> iconFiles = registerIcons();

    iconFiles.forEach(iconName -> {
      Image image = toolkit.getImage(AppIcons.class.getResource("/nl/juraji/pinterestdownloader/resources/icons/" + iconName + ".png"));
      iconMap.put(iconName, new ImageIcon(image));
    });
  }

  public static AppIcons getInstance() {
    if (instance == null) instance = new AppIcons();
    return instance;
  }

  public ImageIcon getIcon(String iconName) {
    return iconMap.getOrDefault(iconName, new ImageIcon());
  }

  private List<String> registerIcons() {
    List<String> icons = new ArrayList<>();

    icons.add("app_lg");
    icons.add("arrow_over");
    icons.add("arrow_to");
    icons.add("delete");
    icons.add("duplicates");
    icons.add("error");
    icons.add("exit");
    icons.add("folder_out");
    icons.add("help");
    icons.add("info");
    icons.add("juraji");
    icons.add("missing_md");
    icons.add("view");
    icons.add("warning");

    return icons;
  }
}
