package nl.juraji.pinterestdownloader.model.pinterest.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Pin {

  public static String[] getFields() {
    return new String[]{"id", "note", "image"};
  }

  @XmlElement(name = "id")
  private String id;

  @XmlElement(name = "note")
  private String note;

  @XmlElement(name = "image")
  private PinImage image;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public PinImage getImage() {
    return image;
  }

  public void setImage(PinImage image) {
    this.image = image;
  }
}
