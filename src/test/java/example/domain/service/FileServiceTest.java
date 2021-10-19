package example.domain.service;

import example.domain.model.File;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class FileServiceTest {

  @Autowired
  private FileService fileService;

  private final String id = "id-test";
  private final String idNull = "nullFile";

  @BeforeEach
  public void init() {
    String name = "TestName.mp3";
    File file = new File(id, name, 125);
    fileService.createFile(file);
  }

  @AfterEach
  public void teardown() {
    fileService.deleteFileById(id);
  }

  @Test
  public void createFile() {
    String name = "TestName.mp3";
    String id = "first-test";
    File file = new File(id, name, 123);

    Integer status = fileService.createFile(file).getStatus();
    Assertions.assertEquals(status, 200);
  }

  @Test
  public void createFileWithoutName() {
    String id = "first-test";
    File file = new File(id, 123);

    Integer status = fileService.createFile(file).getStatus();
    Assertions.assertEquals(status, 400);
  }

  @Test
  public void createFileWithSizeLessZero() {
    String id = "first-test";
    String name = "TestName.mp3";
    File file = new File(id, name, -158);

    Integer status = fileService.createFile(file).getStatus();
    Assertions.assertEquals(status, 400);
  }

  @Test
  public void deleteFile() {
    String id = "first-test";
    Integer status = fileService.deleteFileById(id).getStatus();
    Assertions.assertEquals(status, 200);
  }


  @Test
  public void deleteNullFile() {

    Integer status = fileService.deleteFileById(idNull).getStatus();
    Assertions.assertEquals(status, 404);
  }

  @Test
  public void assignTags() {

    List<String> tags = new ArrayList<>() {{
      add("newTag");
    }};
    Integer status = fileService.assignTags(id, tags).getStatus();
    Assertions.assertEquals(status, 200);
  }

  @Test
  public void assignTagsToNullFile() {

    List<String> tags = new ArrayList<>() {{
      add("newTag");
    }};
    Integer status = fileService.assignTags(idNull, tags).getStatus();
    Assertions.assertEquals(status, 404);
  }

  @Test
  public void deleteTags() {
    List<String> tags = new ArrayList<>() {{
      add("audio");
    }};
    Integer status = fileService.deleteTags(id, tags).getStatus();
    Assertions.assertEquals(status, 200);
  }

  @Test
  public void deleteTagsFromNullFile() {
    List<String> tags = new ArrayList<>() {{
      add("audio");
    }};
    Integer status = fileService.deleteTags(idNull, tags).getStatus();
    Assertions.assertEquals(status, 404);
  }

  @Test
  public void deleteNullTags() {
    List<String> tags = new ArrayList<>() {{
      add("nullTags");
    }};
    Integer status = fileService.deleteTags(id, tags).getStatus();
    Assertions.assertEquals(status, 404);
  }

  @Test
  public void getFiles() {
    List<String> tags = null;
    Integer page = null;
    Integer size = null;
    String q = null;
    Page<File> page1 = fileService.getFiles(tags, page, size, q);
    Pageable pageable = PageRequest.of(0, 10);
    Assertions.assertEquals(page1.getPageable(), pageable);
  }

}
