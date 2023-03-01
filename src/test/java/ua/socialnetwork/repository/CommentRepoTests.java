package ua.socialnetwork.repository;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.socialnetwork.entity.Comment;
import ua.socialnetwork.entity.Post;
import ua.socialnetwork.repo.CommentRepo;
import ua.socialnetwork.repo.PostRepo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentRepoTests {

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private PostRepo postRepo;




    @Test
    @DisplayName("test find all by valid post id")
    public void findAllCommentsByValidPostIdTest(){

        Post post = getPost();
        postRepo.save(post);

        Comment comment1 = getComment(post);
        commentRepo.save(comment1);

        List<Comment> commentList = commentRepo.findAllByPostId(110);
        assertEquals(0, commentList.size());
        assertNotNull(commentList);
    }

    private Post getPost(){
        Post post = new Post();
        post.setBody("body");
        post.setId(110);

        return post;
    }
    private Comment getComment(Post post){
        Comment comment = new Comment();
        comment.setId(110);
        comment.setText("text");
        comment.setPost(post);
        return comment;
    }



}