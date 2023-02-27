package ua.socialnetwork.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.socialnetwork.entity.PostImage;

public interface PostImageRepo extends JpaRepository<PostImage, Integer> {
}
