package ua.socialnetwork.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.socialnetwork.entity.Comment;
import ua.socialnetwork.repo.CommentRepo;
import ua.socialnetwork.service.CommentService;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private CommentRepo commentRepo;

    public Comment create(Comment comment) {
        return commentRepo.save(comment);
    }

    public List<Comment> getCommentsByPostId(int post_id) {
        return commentRepo.findAllByPostId(post_id);
    }

    @Override
    public List<Comment> getAll() {
        return commentRepo.findAll();
    }

    @Override
    public Comment readById(int comment_id) {
        return commentRepo.findById(comment_id).orElseThrow(() -> new NullPointerException("comment not found"));
    }

}
