package example.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import example.domain.model.File;
import example.domain.model.ServiceObject;
import example.domain.service.FileService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/file")
public class FileController {

  private final FileService fileService;


  public FileController(FileService fileService) {
    this.fileService = fileService;
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JsonNode> insertFile(@RequestBody File file) {

    ServiceObject object = fileService.createFile(file);
    return ResponseEntity.status(object.getStatus()).body(object.getJson());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<JsonNode> deleteFileById(@PathVariable String id) {

    ServiceObject object = fileService.deleteFileById(id);
    return ResponseEntity.status(object.getStatus()).body(object.getJson());
  }

  @PostMapping("/{id}/tags")
  public ResponseEntity<JsonNode> assignTagsToFile(@PathVariable String id, @RequestBody List<String> tags) {

    ServiceObject object = fileService.assignTags(id, tags);
    return ResponseEntity.status(object.getStatus()).body(object.getJson());
  }

  @DeleteMapping("/{id}/tags")
  public ResponseEntity<JsonNode> deleteTagsToFile(@PathVariable String id, @RequestBody List<String> tags) {

    ServiceObject object = fileService.deleteTags(id, tags);
    return ResponseEntity.status(object.getStatus()).body(object.getJson());
  }

  @GetMapping()
  public ResponseEntity<Page<File>> getListFiles(
      @RequestParam(value = "tags", required = false) List<String> tags,
      @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size,
      @RequestParam(value = "q", required = false) String q
  ) {

    return ResponseEntity.status(200).body(fileService.getFiles(tags, page, size, q));
  }

}
