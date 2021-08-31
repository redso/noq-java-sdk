package hk.noq.roomq;

public class ValidationResult {
  private String redirectURL;

  public ValidationResult(String redirectURL) {
    this.redirectURL = redirectURL;
  }

  public boolean needRedirect() {
    return redirectURL != null;
  }

  public String getRedirectURL() {
    return redirectURL;
  }
}
