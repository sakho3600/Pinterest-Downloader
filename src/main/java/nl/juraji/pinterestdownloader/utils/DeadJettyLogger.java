package nl.juraji.pinterestdownloader.utils;


public class DeadJettyLogger implements org.eclipse.jetty.util.log.Logger {
  public DeadJettyLogger() {
  }

  @Override
  public String getName() {
    return this.getClass().getSimpleName();
  }

  @Override
  public void warn(String msg, Object... args) {
  }

  @Override
  public void warn(Throwable thrown) {
  }

  @Override
  public void warn(String msg, Throwable thrown) {
  }

  @Override
  public void info(String msg, Object... args) {
  }

  @Override
  public void info(Throwable thrown) {
  }

  @Override
  public void info(String msg, Throwable thrown) {
  }

  @Override
  public boolean isDebugEnabled() {
    return false;
  }

  @Override
  public void setDebugEnabled(boolean enabled) {
  }

  @Override
  public void debug(String msg, Object... args) {
  }

  @Override
  public void debug(String msg, long value) {
  }

  @Override
  public void debug(Throwable thrown) {
  }

  @Override
  public void debug(String msg, Throwable thrown) {
  }

  @Override
  public org.eclipse.jetty.util.log.Logger getLogger(String name) {
    return this;
  }

  @Override
  public void ignore(Throwable ignored) {

  }
}
