package example.repository;

import example.domain.model.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends ElasticsearchRepository<File, String> {
  List<File> findFilesByNameLike(String name);

  void deleteById(String id);

  List<File> findAll();

  Page<File> findFilesByNameLike(String name, Pageable pageable);

}
