package nl.juraji.pinterestdownloader.model.pinterest.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PinImage {

  @XmlElement(name = "original")
  private PinImageAttributes original;

  public PinImageAttributes getOriginal() {
    return original;
  }

  public void setOriginal(PinImageAttributes original) {
    this.original = original;
  }
}
