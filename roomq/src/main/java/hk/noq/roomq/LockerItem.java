package hk.noq.roomq;

public class LockerItem {

  private final String key;
  private final String value;
  private final int limit;
  private final int kvLimit;

  public LockerItem(String key, String value, int limit, int kvLimit)
  {
    this.key = key;
    this.value = value;
    this.limit = limit;
    this.kvLimit = kvLimit;
  }
}
