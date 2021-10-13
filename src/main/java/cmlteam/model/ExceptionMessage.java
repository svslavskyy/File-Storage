package cmlteam.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionMessage extends Message {
  private String error;

  public ExceptionMessage(Boolean success, String error) {
    super(success);
    this.error = error;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }
}
