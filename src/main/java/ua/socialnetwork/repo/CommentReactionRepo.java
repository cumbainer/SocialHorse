package ua.socialnetwork.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.socialnetwork.entity.CommentReactions;
import ua.socialnetwork.entity.User;

import java.util.List;
import java.util.Optional;


public interface CommentReactionRepo extends JpaRepository<CommentReactions, Integer> {

    CommentReactions findCommentReactionByCommentIdAndUser(Integer comment_id, User user);

    List<CommentReactions> findAllByCommentId(Integer comment_id);

    Optional<CommentReactions> findById(Integer integers);
}
