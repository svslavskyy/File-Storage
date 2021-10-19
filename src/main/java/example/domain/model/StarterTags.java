package example.domain.model;

import java.util.HashMap;
import java.util.Map;

public class StarterTags {
  private final Map<String, String> starterTags = new HashMap<>() {{
    put("txt", "document");
    put("doc", "document");
    put("docx", "document");
    put("mp3", "audio");
    put("aac", "audio");
    put("wav", "audio");
    put("flac", "audio");
    put("mp4", "video");
    put("avi", "video");
    put("mkv", "video");
    put("flv", "video");
    put("jpg", "image");
    put("jpeg", "image");
    put("gif", "image");
    put("png", "image");
    put("bmp", "image");
  }};

  public Map<String, String> getStarterTags() {
    return starterTags;
  }
}
