package nl.juraji.pinterestdownloader.model.pinterest.responses;

import java.util.List;

public interface ApiResponse<T> {
  List<T> getData();
  void setData(List<T> data);
  ApiResponsePagingInfo getPage();
  void setPage(ApiResponsePagingInfo page);
}
