package example.domain.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.domain.model.File;
import example.domain.model.ServiceObject;
import example.domain.model.StarterTags;
import example.domain.service.FileService;
import example.repository.FileRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FileServiceDefault implements FileService {
  private final FileRepository fileRepository;

  private final String fileNotFound = "{\"success\" : \"false\",\"error\" : \"file not found\"}";
  private final String successTrue = "{\"success\" : \"true\"}";

  @Autowired
  public FileServiceDefault(FileRepository fileRepository) {
    this.fileRepository = fileRepository;
  }

  private JsonNode generateJson(String string) {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = null;
    try {
      node = mapper.readTree(string);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return node;
  }


  @Override
  public ServiceObject createFile(File file) {
    if (file.getName() == null || file.getName().equals("")) {

      String string = "{\"success\" : \"false\",\"error\" : \"Name can not be null\"}";
      return new ServiceObject(400, generateJson(string));

    } else if (file.getSize() == null || file.getSize() < 0) {

      String string = "{\"success\" : \"false\",\"error\" : \"Size can be more 0\"}";
      return new ServiceObject(400, generateJson(string));

    } else {
      if (file.getId() == null) {
        file.setId(UUID.randomUUID().toString());
      }

      String name = file.getName();
      String extension = FilenameUtils.getExtension(name).toLowerCase();
      StarterTags tags = new StarterTags();
      String tagFromName = tags.getStarterTags().get(extension);
      if (tagFromName != null) {
        file.setTags(Collections.singleton(tagFromName));
      }
      fileRepository.save(file);
    }
    String string = "{\"ID\" : \"" + file.getId() + "\"}";
    return new ServiceObject(200, generateJson(string));
  }

  @Override
  public ServiceObject deleteFileById(String id) {
    if (fileRepository.existsById(id)) {
      fileRepository.deleteById(id);

      return new ServiceObject(200, generateJson(successTrue));
    } else {
      return new ServiceObject(404, generateJson(fileNotFound));
    }
  }

  @Override
  public ServiceObject assignTags(String id, List<String> tags) {
    if (fileRepository.existsById(id)) {
      Set<String> set = new HashSet<>(tags);
      File tempFile = fileRepository.findById(id).orElseThrow();
      set.addAll(tempFile.getTags());
      tempFile.setTags(set);
      fileRepository.save(tempFile);

      return new ServiceObject(200, generateJson(successTrue));
    } else {
      return new ServiceObject(404, generateJson(fileNotFound));
    }
  }

  @Override
  public ServiceObject deleteTags(String id, List<String> tags) {
    if (fileRepository.existsById(id)) {
      Set<String> set = new HashSet<>(tags);
      File tempFile = fileRepository.findById(id).orElseThrow();
      String tagNotFound = "{\"success\" : \"false\",\"error\" : \"tag not found on file\"}";

      if (tempFile.getTags() != null) {
        if (tempFile.getTags().containsAll(set)) {
          tempFile.getTags().removeAll(set);
          fileRepository.save(tempFile);
          return new ServiceObject(200, generateJson(successTrue));
        } else {
          return new ServiceObject(404, generateJson(tagNotFound));
        }
      } else {
        return new ServiceObject(404, generateJson(tagNotFound));
      }

    } else {
      return new ServiceObject(404, generateJson(fileNotFound));
    }
  }

  @Override
  public Page<File> getFiles(List<String> tags,
                             Integer page, Integer size, String q) {

    if (page == null || page < 0) {
      page = 0;
    }
    if (size == null || size < 0) {
      size = 10;
    }

    Pageable pageable = PageRequest.of(page, size);
    if (tags == null || tags.isEmpty()) {
      if (q != null && !q.equals("")) {
        return fileRepository.findFilesByNameLike(q, pageable);
      }
      return fileRepository.findAll(pageable);
    } else {

      Set<String> newTags = new HashSet<>(tags);

      List<File> files = fileRepository.findAll();
      List<File> filesWithTags = new ArrayList<>();
      for (File file : files) {
        if (file.getTags() != null) {
          if (file.getTags().containsAll(newTags)) {
            filesWithTags.add(file);
          }
        }
      }
      if (q != null && !q.equals("")) {
        filesWithTags.retainAll(fileRepository.findFilesByNameLike(q));
      }
      return new PageImpl<>(filesWithTags, PageRequest.of(page, size), filesWithTags.size());
    }

  }
}
