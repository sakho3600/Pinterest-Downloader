package nl.juraji.pinterestdownloader.model;

public class ApiLimits {
  private int totalLimit = -1;
  private int callCount = 0;
  private int remainingCount = -1;

  public int getTotalLimit() {
    return totalLimit;
  }

  public void setTotalLimit(int totalLimit) {
    this.totalLimit = totalLimit;
  }

  public int getCallCount() {
    return callCount;
  }

  public void setCallCount(int callCount) {
    this.callCount = callCount;
  }

  public int getRemainingCount() {
    return remainingCount;
  }

  public void setRemainingCount(int remainingCount) {
    this.remainingCount = remainingCount;
  }
}
