package ua.socialnetwork.service;

import ua.socialnetwork.entity.Comment;

import java.util.List;

public interface CommentService {

    Comment create(Comment comment);
    List<Comment> getCommentsByPostId(int postId);
    List<Comment> getAll();
    Comment readById(int comment_id);
}
