package example.repository;

import example.domain.model.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FileRepository extends ElasticsearchRepository<File, String> {

  void deleteById(String id);

  List<File> findAll();

  @Override
  Page<File> findAll(Pageable pageable);

  Page<File> findFilesByTags(Set<String> tags, Pageable pageable);

  Page<File> findFilesByTagsAndNameLike(Set<String> tags, String name, Pageable pageable);

  Page<File> findFilesByNameLike(String name, Pageable pageable);
}
