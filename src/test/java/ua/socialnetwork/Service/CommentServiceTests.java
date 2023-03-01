package ua.socialnetwork.Service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.socialnetwork.entity.Comment;
import ua.socialnetwork.repo.CommentRepo;
import ua.socialnetwork.service.CommentService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CommentServiceTests {

    @Autowired
    private CommentService commentService;
    @MockBean
    private CommentRepo commentRepo;
    private Comment validComment;
    private Comment invalidComment;

    @BeforeEach
    void init() {
        validComment = new Comment();
        validComment.setId(100);
        validComment.setText("Test comment");
        validComment.setCreatedDate(LocalDateTime.now());

        invalidComment = new Comment();
        invalidComment.setId(200);
        invalidComment.setText("@$#%#@3@#$1");
        invalidComment.setCreatedDate(LocalDateTime.now());
    }

    @BeforeEach
    void setUp() {
        when(commentRepo.findById((int) validComment.getId())).thenReturn(Optional.of(validComment));
        when(commentRepo.findById((int) invalidComment.getId())).thenReturn(Optional.of(invalidComment));
    }

    @Test
    @DisplayName("Create new valid comment")
    public void createValidCommentTest() {
        given(commentRepo.save(validComment)).willReturn(validComment);
        Comment addedComment = commentService.create(validComment);
        verify(commentRepo).save(any(Comment.class));
        Assertions.assertEquals(validComment, addedComment);
    }

    @Test
    @DisplayName("Create new invalid comment")
    public void createNewInvalidCommentTest() {
        Mockito.when(commentRepo.save(invalidComment)).thenThrow(new IllegalArgumentException());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> commentService.create(invalidComment));
    }

    @Test
    @DisplayName("Create invalid text")
    public void createNullSymbolTextTest() {
        Mockito.when(commentRepo.save(null)).thenThrow(new NullPointerException());
        Assertions.assertThrows(NullPointerException.class,
                () -> commentService.create(null));
    }

    @Test
    @DisplayName("Get all comments")
    public void getAllCommentsTest() {
        List<Comment> allComments = new ArrayList<>();
        allComments.add(validComment);

        given(commentRepo.findAll()).willReturn(allComments);
        Assertions.assertEquals(allComments, commentService.getAll());
    }

    @Test
    @DisplayName("Find comment by ID Test")
    public void getCommentById() {
        Mockito.when(commentRepo.save(validComment)).thenThrow(new IllegalArgumentException());
        Assertions.assertEquals(Optional.ofNullable(validComment), commentRepo.findById(100));
    }
}


