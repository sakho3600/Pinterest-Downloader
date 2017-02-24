package nl.juraji.pinterestdownloader.ui.workers;

import nl.juraji.pinterestdownloader.io.ApiHandler;
import nl.juraji.pinterestdownloader.model.ApiLimits;

import javax.swing.*;
import java.util.function.Consumer;

public class ProbeApiLimitsWorker extends SwingWorker<Void, Void> {
  private final Consumer<ApiLimits> onDone;
  private ApiLimits apiLimits;

  public ProbeApiLimitsWorker(Consumer<ApiLimits> onDone) {
    this.onDone = onDone;
  }

  @Override
  protected Void doInBackground() throws Exception {
    ApiHandler apiHandler = ApiHandler.getInstance();
    apiLimits = apiHandler.probeLimits();
    return null;
  }

  @Override
  protected void done() {
    onDone.accept(apiLimits);
  }
}
