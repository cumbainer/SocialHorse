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
import ua.socialnetwork.entity.CommentReactions;
import ua.socialnetwork.repo.CommentReactionRepo;
import ua.socialnetwork.service.CommentReactionService;

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
public class CommentReactionServiceTests {
    @Autowired
    private CommentReactionService commentReactionService;
    @MockBean
    private CommentReactionRepo commentReactionRepo;
    private CommentReactions validReaction;
    private CommentReactions invalidReaction;
    private Comment testComment;

    @BeforeEach
    void init() {
        ////toDo Maybe need improve ***********
        testComment = new Comment();
        testComment.setCreatedDate(LocalDateTime.now());
        testComment.setId(200);
        testComment.setText("testComment");
        ////toDo Maybe need improve ************

        validReaction = new CommentReactions();
        validReaction.setId(100);
        validReaction.setReaction(true);
        validReaction.setComment(testComment);

        invalidReaction = new CommentReactions();
        invalidReaction.setId(200);
        invalidReaction.setReaction(null);
        invalidReaction.setComment(testComment);
    }

    @BeforeEach
    void setUp() {
        when(commentReactionRepo.findById(validReaction.getId())).thenReturn(Optional.of(validReaction));
        when(commentReactionRepo.findById(invalidReaction.getId())).thenReturn(Optional.of(invalidReaction));
    }

    @Test
    @DisplayName("Create new valid reaction")
    public void createValidReactionTest() {
        given(commentReactionRepo.save(validReaction)).willReturn(validReaction);
        CommentReactions addedReaction = commentReactionService.save(validReaction);
        verify(commentReactionRepo).save(any(CommentReactions.class));
        Assertions.assertEquals(validReaction, addedReaction);
    }

    @Test
    @DisplayName("Create new invalid reaction")
    public void creatNewInvalidReactionTest() {
        Mockito.when(commentReactionRepo.save(invalidReaction)).thenThrow(new NullPointerException());
        Assertions.assertThrows(NullPointerException.class,
                () -> commentReactionService.save(invalidReaction));
    }

    @Test
    @DisplayName("Get all reaction by commentID")
    public void findAllByCommentId() {
        List<CommentReactions> allReactions = new ArrayList<>();
        allReactions.add(validReaction);

        given(commentReactionRepo.findAllByCommentId((int) testComment.getId())).willReturn(allReactions);
        Assertions.assertEquals(allReactions, commentReactionRepo.findAllByCommentId((int) testComment.getId()));
    }
}
