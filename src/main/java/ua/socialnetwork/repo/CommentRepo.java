package ua.socialnetwork.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.socialnetwork.entity.Comment;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Integer> {
    @Query(value = "select * from comment where post_id = ?1 ORDER BY comment_id DESC", nativeQuery = true)
    List<Comment> findAllByPostId(Integer id);
}
