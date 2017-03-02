package nl.juraji.pinterestdownloader.model.pinterest.responses;

import java.util.List;

public class OkResponse implements ApiResponse<Void> {
  // No data is kept, this class itself acts as OK flag

  @Override
  public List<Void> getData() {
    return null;
  }

  @Override
  public void setData(List<Void> data) {
  }

  @Override
  public ApiResponsePagingInfo getPage() {
    return null;
  }

  @Override
  public void setPage(ApiResponsePagingInfo page) {
  }
}
