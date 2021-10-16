package example.domain.service.impl;

import example.domain.exceptions.model.ExceptionMessage;
import example.domain.exceptions.model.Message;
import example.domain.model.File;
import example.domain.model.IdFile;
import example.domain.service.FileService;
import example.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FileServiceDefault implements FileService {
  private final FileRepository fileRepository;

  @Autowired
  public FileServiceDefault(FileRepository fileRepository) {
    this.fileRepository = fileRepository;
  }

  @Override
  public ResponseEntity createFile(File file) {
    if (file.getName() == null) {
      return ResponseEntity.status(400)
          .body(new ExceptionMessage(false, "Name can not be null"));
    } else if (file.getSize() < 0) {
      return ResponseEntity.status(400)
          .body(new ExceptionMessage(false, "Size can be more 0"));
    } else {
      if (file.getId() == null) {
        file.setId(UUID.randomUUID().toString());
      }
      String name = file.getName();
      String[] subStr;
      subStr = name.split("\\.");


      switch (subStr[subStr.length - 1]) {
        case "txt":
          file.setTags(Collections.singleton("text"));
          break;
        case "mp3":
        case "aac":
        case "wav":
        case "flac":
          file.setTags(Collections.singleton("audio"));
          break;
        case "mp4":
        case "avi":
        case "mkv":
        case "flv":
          file.setTags(Collections.singleton("video"));
          break;
        case "doc":
        case "docx":
          file.setTags(Collections.singleton("document"));
          break;
        case "jpg":
        case "jpeg":
        case "gif":
        case "png":
        case "bmp":
          file.setTags(Collections.singleton("image"));
          break;

      }
      fileRepository.save(file);
    }

    return ResponseEntity.status(200).body(new IdFile(file.getId()));
  }

  @Override
  public ResponseEntity getFileById(String id) {
    if (fileRepository.existsById(id)) {
      return ResponseEntity.status(200).body(fileRepository.findById(id));
    } else {
      return ResponseEntity.status(400).body(new ExceptionMessage(false, "file not found"));
    }
  }

  @Override
  public ResponseEntity deleteFileById(String id) {
    if (fileRepository.existsById(id)) {
      fileRepository.deleteById(id);
      return ResponseEntity.status(200).body(new Message(true));
    } else {
      return ResponseEntity.status(404).body(new ExceptionMessage(false, "file not found"));
    }
  }

  @Override
  public ResponseEntity assignTags(String id, List<String> tags) {
    if (fileRepository.existsById(id)) {
      Set<String> set = new HashSet<>(tags);
      File tempFile = fileRepository.findById(id).orElseThrow();
      set.addAll(tempFile.getTags());
      tempFile.setTags(set);
      fileRepository.save(tempFile);
      return ResponseEntity.status(200).body(new Message(true));
    } else {
      return ResponseEntity.status(404).body(new ExceptionMessage(false, "file not found"));
    }
  }

  @Override
  public ResponseEntity deleteTags(String id, List<String> tags) {
    if (fileRepository.existsById(id)) {
      Set<String> set = new HashSet<>(tags);
      File tempFile = fileRepository.findById(id).orElseThrow();
      if (tempFile.getTags().containsAll(set)) {
        tempFile.getTags().removeAll(set);
        fileRepository.save(tempFile);
        return ResponseEntity.status(200).body(new Message(true));
      } else {
        return ResponseEntity.status(404).body(new ExceptionMessage(false, "tag not found on file"));
      }
    } else {
      return ResponseEntity.status(404).body(new ExceptionMessage(false, "file not found"));
    }
  }

  @Override
  public ResponseEntity getFiles(Optional<List<String>> tags,
                                 Optional<Integer> page, Optional<Integer> size, Optional<String> q) {
    int newPage = page.orElse(0);
    int newSize = size.orElse(10);

    if (tags.isPresent()) {
      Set<String> set = new HashSet<>(tags.get());
      List<File> files = fileRepository.findAll();
      List<File> filesWithTags = new ArrayList<>();
      for (File file : files) {
        if (file.getTags() != null) {
          if (file.getTags().containsAll(set)) {
            filesWithTags.add(file);
          }
        }
      }

      q.ifPresent(s -> filesWithTags.retainAll(fileRepository.findFilesByNameLike(s)));
      final Page<File> page1 = new PageImpl<>(filesWithTags, PageRequest.of(newPage, newSize), filesWithTags.size());
      return ResponseEntity.status(200).body(page1);


    } else {
      if (q.isPresent()) {
        List<File> temp = fileRepository.findFilesByNameLike(q.get());
        final Page<File> page1 = new PageImpl<>(temp, PageRequest.of(newPage, newSize), temp.size());
        return ResponseEntity.status(200).body(page1);
      } else {
        return ResponseEntity.status(200).body(fileRepository.findAll(PageRequest.of(newPage, newSize)));

      }

    }
  }
}
