package nl.juraji.pinterestdownloader.configuration;

import nl.juraji.pinterestdownloader.PinterestDownloader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;

public final class AppData {
  private static final Charset CHARSET = Charset.forName("UTF-8");
  private static AppData instance;
  private final Properties properties;

  private AppData() {
    InputStream propFileStream = AppData.class.getResourceAsStream("/nl/juraji/pinterestdownloader/PinterestDownloader.properties");
    properties = new Properties();
    try {
      properties.load(propFileStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Retrieve a property from the application data file
   *
   * @param propertyName The name of the property to retrieve
   * @return The value associated with the name, else null
   */
  public static String get(String propertyName) {
    if (instance == null) instance = new AppData();
    return instance.properties.getProperty(PinterestDownloader.class.getSimpleName() + "." + propertyName);
  }

  public static String[] getImgFileTypes() {
    return AppData.get("ImgFileTypes").split(",");
  }

  public static Charset getCharset() {
    return CHARSET;
  }

  public static String getDirSeparator() {
    return System.getProperty("file.separator");
  }

  public static String getNewlineSeparator() {
    return System.lineSeparator();
  }
}
