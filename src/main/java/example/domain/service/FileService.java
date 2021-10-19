package example.domain.service;

import example.domain.model.File;
import example.domain.model.ServiceObject;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FileService {
  ServiceObject createFile(File file);

  ServiceObject deleteFileById(String id);

  ServiceObject assignTags(String id, List<String> tags);

  ServiceObject deleteTags(String id, List<String> tags);

  Page<File> getFiles(List<String> tags, Integer page, Integer size, String q);
}
