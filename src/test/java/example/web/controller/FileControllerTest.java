package example.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.domain.model.File;
import example.domain.service.FileService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
public class FileControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private FileService fileService;

  private final String id = "id-test";

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
  public void insertFile() throws Exception {
    String name = "TestName.mp3";
    String id = "first-test";
    File file = new File(id, name, 125);
    mockMvc.perform(post("/file")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(file)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.ID").value(id));
  }

  @Test
  public void exceptionInsertFileNoName() throws Exception {
    File file = new File(id, 125);
    mockMvc.perform(post("/file")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(file)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(jsonPath("$.error").value("Name can not be null"));
  }

  @Test
  public void exceptionInsertFileSizeLessZero() throws Exception {
    String name = "TestName.mp3";
    File file = new File(id, name, -125);
    mockMvc.perform(post("/file")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(file)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.success").value("false"))
        .andExpect(jsonPath("$.error").value("Size can be more 0"));
  }

  @Test
  public void deleteFile() throws Exception {
    mockMvc.perform(delete("/file/" + id)
        .contentType(MediaType.TEXT_PLAIN)
        .content(id))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.success").value("true"));
  }

  @Test
  public void assignTagsToFile() throws Exception {

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
        .andExpect(jsonPath("$.success").value("true"));
  }

  @Test
  public void deleteTagsToFile() throws Exception {
    List<String> tags = new ArrayList<>();
    tags.add("newtag");
    tags.add("favorite");

    fileService.assignTags(id, tags);

    mockMvc.perform(delete("/file/" + id + "/tags")
        .contentType(MediaType.TEXT_PLAIN)
        .content(id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(tags)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.success").value("true"));
  }

  @Test
  public void getListFiles() throws Exception {

    mockMvc.perform(get("/file"))
        .andExpect(status().is(200));
  }


}
