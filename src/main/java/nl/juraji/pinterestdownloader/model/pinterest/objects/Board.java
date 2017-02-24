package nl.juraji.pinterestdownloader.model.pinterest.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Board {

  public static String[] getFields() {
    return new String[]{"id", "name", "creator(username)"};
  }

  @XmlElement(name = "id")
  private String id;

  @XmlElement(name = "name")
  private String name;

  @XmlElement(name = "creator")
  private User creator;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public User getCreator() {
    return creator;
  }

  public void setCreator(User creator) {
    this.creator = creator;
  }

  public String getBoardUri() {
    String cleanName = name.replaceAll("[^a-zA-Z0-9-\\s]", "");
    return creator.getUsername() + "/" + String.join("-", cleanName.split("\\s+")).toLowerCase();
  }
}
