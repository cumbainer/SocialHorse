package ua.socialnetwork.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.socialnetwork.entity.User;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    @Query("""
    SELECT u FROM User u WHERE u.username = :username
    """)
    Optional<User> findUserByUsername(String username);
}
