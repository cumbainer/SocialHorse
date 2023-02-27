package ua.socialnetwork.service;

import ua.socialnetwork.entity.CommentReactions;
import ua.socialnetwork.entity.User;

import java.util.List;

public interface CommentReactionService{

    CommentReactions save(CommentReactions reaction);

    CommentReactions findCommentReactionByCommentIdAndUser(Integer comment_id, User user);

    List<CommentReactions> findAllByCommentId(Integer comment_id);

}
