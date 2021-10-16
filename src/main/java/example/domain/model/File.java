package example.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Objects;
import java.util.Set;

@Document(indexName = "file")
public class File {
  @Id
  private String id;
  @Field(type = FieldType.Text)
  private String name;
  @Field(type = FieldType.Integer)
  private Integer size;
  @Field(type = FieldType.Nested)
  private Set<String> tags;

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

  @Override
  public String toString() {
    return "File{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", size=" + size +
        ", tags=" + tags +
        '}';
  }

  public File() {
  }

  public File(String id, String name, Integer size) {
    this.id = id;
    this.name = name;
    this.size = size;
  }

  public File(String id, Integer size) {
    this.id = id;
    this.size = size;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    File file = (File) o;
    return Objects.equals(id, file.id) && Objects.equals(name, file.name) && Objects.equals(size, file.size) && Objects.equals(tags, file.tags);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, size, tags);
  }
}
