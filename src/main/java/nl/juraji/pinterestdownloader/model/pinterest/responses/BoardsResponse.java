package nl.juraji.pinterestdownloader.model.pinterest.responses;

import nl.juraji.pinterestdownloader.model.pinterest.objects.Board;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BoardsResponse implements ApiResponse<Board> {

  @XmlElement(name = "data")
  private List<Board> data;

  @XmlElement(name = "page")
  private ApiResponsePagingInfo page;

  @Override
  public List<Board> getData() {
    return data;
  }

  @Override
  public void setData(List<Board> data) {
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
