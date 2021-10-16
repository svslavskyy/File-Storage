package example.domain.service;

import example.domain.model.File;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface FileService {
  ResponseEntity createFile(File file);

  ResponseEntity getFileById(String id);

  ResponseEntity deleteFileById(String id);

  ResponseEntity assignTags(String id, List<String> tags);

  ResponseEntity deleteTags(String id, List<String> tags);

  ResponseEntity getFiles(Optional<List<String>> tags, Optional<Integer> page, Optional<Integer> size, Optional<String> q);
}
