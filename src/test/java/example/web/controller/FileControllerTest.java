package example.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.domain.model.File;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @Order(1)
  @Test
  public void insertFile() throws Exception {
    String name = "TestName.mp3";
    String id = "id-test";
    File file = new File(id, name, 125);
    mockMvc.perform(post("/file")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(file)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.id").value(id));

  }

  @Order(2)
  @Test
  public void exceptionInsertFileNoName() throws Exception {
    String id = "id-test";
    File file = new File(id, 125);
    mockMvc.perform(post("/file")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(file)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.error").value("Name can not be null"));
  }

  @Order(3)
  @Test
  public void exceptionInsertFileSizeLessZero() throws Exception {
    String name = "TestName.mp3";
    String id = "id-test";
    File file = new File(id, name, -125);
    mockMvc.perform(post("/file")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(file)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.error").value("Size can be more 0"));

  }

  @Order(4)
  @Test
  public void getFileById() throws Exception {
    String name = "TestName.mp3";
    String id = "id-test";
    mockMvc.perform(get("/file/" + id)
        .contentType(MediaType.TEXT_PLAIN)
        .content(id))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.name").value(name))
        .andExpect(jsonPath("$.tags").value("audio"));

  }

  @Order(5)
  @Test
  public void exceptionGetFileById() throws Exception {
    String id = "not-id-test";
    mockMvc.perform(get("/file/" + id)
        .contentType(MediaType.TEXT_PLAIN)
        .content(id))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.error").value("file not found"));
  }


  /***
   * Since the request returns [newtag, audio, favorite] and we can compare only
   * with ["newtag", "audio", "favorite"], the test checks for the presence of tags
   *
   * @throws Exception
   */
  @Order(6)
  @Test
  public void assignTagsToFile() throws Exception {
    String id = "id-test";
    List<String> tags = new ArrayList<>();
    tags.add("newtag");
    tags.add("favorite");

    List<String> answerTags = new ArrayList<>(tags);
    answerTags.add("audio");
    mockMvc.perform(post("/file/" + id + "/tags")
        .contentType(MediaType.TEXT_PLAIN)
        .content(id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(tags)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.success").value(true));

    mockMvc.perform(get("/file/" + id)
        .contentType(MediaType.TEXT_PLAIN)
        .content(id))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.tags").exists());

  }

  @Order(7)
  @Test
  public void deleteTagsToFile() throws Exception {
    String id = "id-test";
    List<String> tags = new ArrayList<>();
    tags.add("newtag");
    tags.add("favorite");
    tags.add("audio");


    mockMvc.perform(delete("/file/" + id + "/tags")
        .contentType(MediaType.TEXT_PLAIN)
        .content(id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(tags)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.success").value(true));

    mockMvc.perform(get("/file/" + id)
        .contentType(MediaType.TEXT_PLAIN)
        .content(id))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.tags").isEmpty());

  }

  @Order(8)
  @Test
  public void getListFiles() throws Exception {
    String name = "TestName.jpg";
    String id = "id-test-jpg";
    File file = new File(id, name, 125);
    mockMvc.perform(post("/file")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(file)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.id").value(id));
    String name1 = "TestName1.jpg";
    String id1 = "id-test-jpg1";
    File file1 = new File(id, name, 125);
    mockMvc.perform(post("/file")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(file)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.id").value(id));

    String tags = "image";

    mockMvc.perform(get("/file?tags=" + tags)
        .contentType(MediaType.TEXT_PLAIN)
        .content(tags))
        .andExpect(status().is(200));

  }


}
