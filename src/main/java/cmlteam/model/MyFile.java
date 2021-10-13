package cmlteam.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_EMPTY)

public class MyFile {
  private String id;
  private String name;
  private Integer size;
  private Set<String> tags;

  public MyFile(String id, String name, Integer size, Set<String> tags) {
    this.id = id;
    this.name = name;
    this.size = size;
    this.tags = tags;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public Set<String> getTags() {
    return tags;
  }

  public void setTags(Set<String> tags) {
    this.tags = tags;
  }
}
