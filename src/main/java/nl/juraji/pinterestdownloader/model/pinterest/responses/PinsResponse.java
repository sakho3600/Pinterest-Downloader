package nl.juraji.pinterestdownloader.model.pinterest.responses;

import nl.juraji.pinterestdownloader.model.pinterest.objects.Pin;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PinsResponse implements ApiResponse<Pin> {

  @XmlElement(name = "data")
  private List<Pin> data;

  @XmlElement(name = "page")
  private ApiResponsePagingInfo page;

  @Override
  public List<Pin> getData() {
    return data;
  }

  @Override
  public void setData(List<Pin> data) {
    this.data = data;
  }

  @Override
  public ApiResponsePagingInfo getPage() {
    return page;
  }

  @Override
  public void setPage(ApiResponsePagingInfo page) {
    this.page = page;
  }
}
