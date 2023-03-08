package ua.socialnetwork.repo;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.socialnetwork.entity.Post;

import java.util.List;


public interface PostRepo extends JpaRepository<Post, Integer> {
    List<Post> getPostsByUserId(Integer id);
    List<Post> getPostsByUser_Username(String username, Sort sort);


}
