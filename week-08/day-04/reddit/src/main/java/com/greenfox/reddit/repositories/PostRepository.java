package com.greenfox.reddit.repositories;

import com.greenfox.reddit.models.Post;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {

  List<Post> findAllByInitialPostIsTrueOrderByVoteDesc();

  List<Post> findPostsByParentPost(Post parentPost);
}
