package nl.juraji.pinterestdownloader;

import nl.juraji.pinterestdownloader.io.ApiHandler;
import nl.juraji.pinterestdownloader.model.pinterest.objects.Pin;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TemporaryTests {

  @Test
  public void deletePin() throws Exception {
    ApiHandler apiHandler = ApiHandler.getInstance();
    Pin pin = new Pin();
    pin.setId("677932550128041237");

    boolean b = apiHandler.deletePin(pin);

    assertTrue(b);
  }
}
