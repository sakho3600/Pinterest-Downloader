package nl.juraji.pinterestdownloader.utils;

import nl.juraji.pinterestdownloader.configuration.AppData;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public final class UriUtils {

  private UriUtils() {
  }

  /**
   * Encode the given Map to a url querystring.
   *
   * @param query A map containing the key-value pairs to pack
   * @return The resulting querystring (without ?) or an empty string if there were no values
   */
  public static String createQueryString(Map<String, Object> query) {
    return query.entrySet().stream()
        .map(e -> urlEncodeUTF8(e.getKey()) + "=" + urlEncodeUTF8(String.valueOf(e.getValue())))
        .reduce((q1, q2) -> q1 + "&" + q2)
        .orElse("");
  }

  /**
   * Encode the given Map to a url querystring and append it to the base url.
   * Inserts a "?" if its missing
   *
   * @param baseUri The uri to append the query to
   * @param query A map containing the key-value pairs to pack
   * @return The resulting uri
   */
  public static String appendQueryString(String baseUri, Map<String, Object> query) {
    return new MutableString(baseUri)
        .appendIfMissing("?")
        .append(createQueryString(query))
        .toString();
  }

  public static String urlEncodeUTF8(String s) {
    try {
      return URLEncoder.encode(s, AppData.getCharset().name());
    } catch (UnsupportedEncodingException e) {
      throw new UnsupportedOperationException(e);
    }
  }
}
