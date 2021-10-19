package example.domain.model;

import com.fasterxml.jackson.databind.JsonNode;

public class ServiceObject {
  Integer status;
  JsonNode json;

  public ServiceObject(Integer status, JsonNode json) {
    this.status = status;
    this.json = json;
  }

  public Integer getStatus() {
    return status;
  }

  public JsonNode getJson() {
    return json;
  }
}
