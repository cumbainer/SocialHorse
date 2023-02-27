package ua.socialnetwork.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.socialnetwork.entity.CommentReactions;
import ua.socialnetwork.entity.User;
import ua.socialnetwork.repo.CommentReactionRepo;
import ua.socialnetwork.service.CommentReactionService;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentReactionServiceImpl implements CommentReactionService {

    private CommentReactionRepo commentReactionRepo;

    @Override
    public CommentReactions save(CommentReactions reactions) {
        return commentReactionRepo.save(reactions);
    }

    @Override
    public CommentReactions findCommentReactionByCommentIdAndUser(Integer comment_id, User user) {
        return commentReactionRepo.findCommentReactionByCommentIdAndUser(comment_id, user);
    }

    @Override
    public List<CommentReactions> findAllByCommentId(Integer comment_id) {
        return commentReactionRepo.findAllByCommentId(comment_id);
    }
}
