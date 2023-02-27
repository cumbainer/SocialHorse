package ua.socialnetwork.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.socialnetwork.entity.UserImage;

@Transactional
public interface  UserImageRepo extends JpaRepository<UserImage, Integer > {
}
