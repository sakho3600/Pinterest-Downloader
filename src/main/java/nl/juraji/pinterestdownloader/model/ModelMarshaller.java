package nl.juraji.pinterestdownloader.model;

import nl.juraji.pinterestdownloader.configuration.AppData;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

public final class ModelMarshaller<T> {
  private final JAXBContext context;
  private final Class<T> boundClass;

  static {
    System.setProperty("javax.xml.bind.context.factory", AppData.get("JAXBContextFactory"));
  }

  public ModelMarshaller(Class<T> bindToClass) throws JAXBException {
    boundClass = bindToClass;
    context = JAXBContext.newInstance(bindToClass);
  }

  public String marshal(T jaxbElement) throws JAXBException {
    Marshaller marshaller = context.createMarshaller();
    Writer outputStream = new StringWriter();

    marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
    marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    marshaller.marshal(jaxbElement, outputStream);
    return outputStream.toString();
  }

  public T unMarshal(String input, String mediaType) throws JAXBException {
    Unmarshaller unmarshaller = context.createUnmarshaller();
    StreamSource source = new StreamSource(new StringReader(input));

    unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, mediaType);
    unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, false);

    //noinspection unchecked
    return unmarshaller.unmarshal(source, boundClass).getValue();
  }
}
