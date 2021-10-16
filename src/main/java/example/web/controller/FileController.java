package example.web.controller;

import example.domain.model.File;
import example.domain.service.FileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/file")
public class FileController {

  private final FileService fileService;


  public FileController(FileService fileService) {
    this.fileService = fileService;
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity insertFile(@RequestBody File file) {

    return fileService.createFile(file);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity deleteFileById(@PathVariable String id) {

    return fileService.deleteFileById(id);
  }

  @GetMapping("/{id}")
  public ResponseEntity getFileById(@PathVariable String id) {

    return fileService.getFileById(id);
  }

  @PostMapping("/{id}/tags")
  public ResponseEntity assignTagsToFile(@PathVariable String id, @RequestBody List<String> tags) {

    return fileService.assignTags(id, tags);
  }

  @DeleteMapping("/{id}/tags")
  public ResponseEntity deleteTagsToFile(@PathVariable String id, @RequestBody List<String> tags) {

    return fileService.deleteTags(id, tags);
  }

  @GetMapping()
  public ResponseEntity getListFiles(
      @RequestParam(value = "tags") Optional<List<String>> tags,
      @RequestParam(value = "page") Optional<Integer> page,
      @RequestParam(value = "size") Optional<Integer> size,
      @RequestParam(value = "q") Optional<String> q
  ) {

    return fileService.getFiles(tags, page, size, q);
  }

}
