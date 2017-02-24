package nl.juraji.pinterestdownloader.utils;

public class BasicJLabelHtml {
  private final StringBuilder stringBuilder;

  public BasicJLabelHtml() {
    stringBuilder = new StringBuilder("<html>");
  }

  public void addLine(String text) {
    stringBuilder.append(text).append("<br />");
  }

  public String build() {
    stringBuilder.append("</html>");
    return stringBuilder.toString();
  }
}
