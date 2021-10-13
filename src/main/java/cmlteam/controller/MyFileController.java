package cmlteam.controller;


import cmlteam.dao.MyFileDao;
import cmlteam.model.ExceptionMessage;
import cmlteam.model.Message;
import cmlteam.model.MyFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class MyFileController {

  private MyFileDao myFileDao;

  public MyFileController(MyFileDao fileDao) {
    this.myFileDao = fileDao;
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity insertFile(@RequestBody MyFile myFile) {
    if (myFile.getName() == null) {
      ExceptionMessage exceptionMessage = new ExceptionMessage(false, "Name cant be null");
      return ResponseEntity.status(400).body(exceptionMessage);
    }
    if (myFile.getSize() < 0) {
      ExceptionMessage exceptionMessage = new ExceptionMessage(false, "Size can be more 0");
      return ResponseEntity.status(400).body(exceptionMessage);
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(myFileDao.insertFile(myFile));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity deleteFileById(@PathVariable String id) {
    if (myFileDao.getFileById(id) != null) {
      return ResponseEntity.status(200).body(myFileDao.deleteFileById(id));
    } else {
      return ResponseEntity.status(404).body(myFileDao.deleteFileById(id));
    }

  }

  @PostMapping("/{id}/tags")
  @ResponseBody
  public ResponseEntity assignTagsToFile(@PathVariable String id, @RequestBody List<String> tags) {

    myFileDao.assignTags(id, tags);

    return ResponseEntity.status(200).body(new Message(true));
  }

  @DeleteMapping("/{id}/tags")
  @ResponseBody
  public ResponseEntity deleteTagsToFile(@PathVariable String id, @RequestBody List<String> tags) {

    if (myFileDao.deleteTags(id, tags)) {
      return ResponseEntity.status(200).body(new Message(true));
    } else {
      return ResponseEntity.status(400).body(new ExceptionMessage(false, "tag not found on file"));
    }

  }


  @GetMapping("/{id}")
  public Map<String, Object> getFileById(@PathVariable String id) {
    return myFileDao.getFileById(id);
  }


}
