package nl.juraji.pinterestdownloader.utils;

import org.eclipse.jetty.util.ssl.SslContextFactory;

public class SslTlsContextFactory extends SslContextFactory {
  public static final String TLS_SUPPORT = "TLSv1.2";

  public SslTlsContextFactory() {
    this.setIncludeProtocols(TLS_SUPPORT);
    this.setTrustAll(true);
  }
}
