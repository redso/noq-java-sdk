package hk.noq.roomq;

public class Locker {
  private final String clientID;
  private final String apiKey;
  private final String token;
  private final String url;

  public Locker(String clientID, String apiKey, String token, String url) {
    this.clientID = clientID;
    this.apiKey = apiKey;
    this.token = token;
    this.url = url;
  }
}
