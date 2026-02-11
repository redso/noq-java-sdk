package hk.noq.roomq;

public class CookieConfig {

  private int maxAge = 12 * 60 * 60;
  private Boolean httpOnly;
  private Boolean secure;
  private String path;
  private String domain;
  private String sameSite;

  public int getMaxAge() {
    return maxAge;
  }

  public void setMaxAge(int maxAge) {
    this.maxAge = maxAge;
  }

  public Boolean getHttpOnly() {
    return httpOnly;
  }

  public void setHttpOnly(Boolean httpOnly) {
    this.httpOnly = httpOnly;
  }

  public Boolean getSecure() {
    return secure;
  }

  public void setSecure(Boolean secure) {
    this.secure = secure;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getSameSite() {
    return sameSite;
  }

  public void setSameSite(String sameSite) {
    this.sameSite = sameSite;
  }
}
