public class Request {
  String url;
  String operation;
  int latency;

  public Request(String url, String operation, int latency) {
    this.url = url;
    this.operation = operation;
    this.latency = latency;
  }

  public String getUrl() {
    return url;
  }

  public String getOperation() {
    return operation;
  }

  public int getLatency() {
    return latency;
  }
}
