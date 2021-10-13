package cmlteam.dao;

import cmlteam.model.ExceptionMessage;
import cmlteam.model.IdFile;
import cmlteam.model.Message;
import cmlteam.model.MyFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MyFileDao {

  private final String INDEX = "filedata";
  private final String TYPE = "files";

  private RestHighLevelClient restHighLevelClient;

  private ObjectMapper objectMapper;

  public MyFileDao(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
    this.objectMapper = objectMapper;
    this.restHighLevelClient = restHighLevelClient;
  }

  public IdFile insertFile(MyFile myFile) {
    myFile.setId(UUID.randomUUID().toString());
    myFile.setTags(new HashSet<>());
    Map<String, Object> dataMap = objectMapper.convertValue(myFile, Map.class);
    IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, myFile.getId())
        .source(dataMap);
    try {
      restHighLevelClient.index(indexRequest);
    } catch (ElasticsearchException e) {
      e.getDetailedMessage();
    } catch (java.io.IOException ex) {
      ex.getLocalizedMessage();
    }
    return new IdFile(myFile.getId());
  }

  public Message assignTags(String id, List<String> tags) {

    UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, id)
        .fetchSource(true);    // Fetch Object after its update

    Map<String, Object> map = getFileById(id);
    Set<String> set = new TreeSet<>(tags);
    if (map.containsKey("tags")) {
      set.addAll((Collection<? extends String>) map.get("tags"));
    }
    return function(updateRequest, map, set);

  }


  public boolean deleteTags(String id, List<String> tags) {

    UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, id)
        .fetchSource(true);    // Fetch Object after its update

    Map<String, Object> map = getFileById(id);
    Set<String> setTagsOut = new TreeSet<>(tags);
    Set<String> tagsIn = null;
    if (map.containsKey("tags")) {
      tagsIn = new HashSet<>((ArrayList<String>) map.get("tags"));
      if (tagsIn.containsAll(setTagsOut)) {
        System.out.println(tagsIn);
        System.out.println(setTagsOut);
        tagsIn.removeAll(setTagsOut);
      } else {
        return false;
      }

    }
    MyFile myFile = new MyFile((String) map.get("id"), (String) map.get("name"), (Integer) map.get("size"), tagsIn);

    try {
      String fileJson = objectMapper.writeValueAsString(myFile);
      updateRequest.doc(fileJson, XContentType.JSON);
      UpdateResponse updateResponse = restHighLevelClient.update(updateRequest);
      updateResponse.getGetResult().sourceAsMap();
      return true;
    } catch (JsonProcessingException e) {
      e.getMessage();
    } catch (java.io.IOException e) {
      e.getLocalizedMessage();
    }
    return false;

  }

  private Message function(UpdateRequest updateRequest, Map<String, Object> map, Set<String> tagsIn) {
    MyFile myFile = new MyFile((String) map.get("id"), (String) map.get("name"), (Integer) map.get("size"), tagsIn);

    try {
      String fileJson = objectMapper.writeValueAsString(myFile);
      updateRequest.doc(fileJson, XContentType.JSON);
      UpdateResponse updateResponse = restHighLevelClient.update(updateRequest);
      updateResponse.getGetResult().sourceAsMap();
      return new Message(true);
    } catch (JsonProcessingException e) {
      e.getMessage();
    } catch (java.io.IOException e) {
      e.getLocalizedMessage();
    }
    return new ExceptionMessage(false, "error");
  }

//  public List<Map> getFileByTags(List<String> tags) throws IOException {
//
//    SearchRequest searchRequest = new SearchRequest(INDEX);
//    searchRequest.types(TYPE);
//    Response response = restHighLevelClient.getLowLevelClient().performRequest("GET","filedata/files/_search");
//    System.out.println(response);
//    System.out.println(response.toString());
//
//////    Request getRequest = new Request("GET","filedata/files/_search");
////    SearchResponse getResponse = null;
////    try {
////      getResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
////    } catch (java.io.IOException e) {
////      e.getLocalizedMessage();
////    }
////    SearchHit[] searchHits = getResponse.getHits().getHits();
////    List<Map> list = new ArrayList();
////    for(SearchHit sh : searchHits) {
////      list.add(sh.getSourceAsMap());
////    }
////    return list;
//    return null;
//  }
//
////  public Page getFileByTags(List<String> tags){
////    GetRequest getRequest = new GetRequest(INDEX, TYPE, tags);
////  }
//

  public Map<String, Object> getFileById(String id) {
    GetRequest getRequest = new GetRequest(INDEX, TYPE, id);
    GetResponse getResponse = null;
    try {
      getResponse = restHighLevelClient.get(getRequest);
    } catch (java.io.IOException e) {
      e.getLocalizedMessage();
    }
    assert getResponse != null;
    Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
    System.out.println(sourceAsMap);
    return sourceAsMap;
  }


  public Message deleteFileById(String id) {
    if (getFileById(id) == null) {
      return new ExceptionMessage(false, "file not found");
    }
    DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, id);
    try {
      restHighLevelClient.delete(deleteRequest);
    } catch (java.io.IOException e) {
      e.getLocalizedMessage();
    }
    return new Message(true);
  }

}
