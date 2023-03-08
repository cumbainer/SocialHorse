package ua.socialnetwork.controller;

import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.socialnetwork.entity.CommentReactions;
import ua.socialnetwork.entity.User;
import ua.socialnetwork.repo.UserRepo;
import ua.socialnetwork.security.SecurityUser;
import ua.socialnetwork.service.CommentReactionService;
import ua.socialnetwork.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/reaction")
@AllArgsConstructor
public class ReactionController {
    private final CommentReactionService commentReactionService;
    private final CommentService commentService;
    private final UserRepo userRepo;

    @PostMapping("/add")
    public JSONObject setReaction(@RequestParam("comment_id") int commentId,
                                  @RequestParam("reaction") String reaction, @AuthenticationPrincipal SecurityUser authUser) {

        JSONObject reactionObject = new JSONObject();
        User user = userRepo.getReferenceById(authUser.getId());
        CommentReactions commentReaction = commentReactionService.findCommentReactionByCommentIdAndUser(commentId, user);

        if (commentReaction == null) {
            commentReaction = new CommentReactions();
            commentReaction.setComment(commentService.readById(commentId));
            commentReaction.setUser(user);
        }

        boolean resultReaction = (reaction.equals("like"));
        if ((null == commentReaction.getReaction()) || (resultReaction != commentReaction.getReaction())) {
            commentReaction.setReaction(resultReaction);
        }
        commentReactionService.save(commentReaction);

        List<CommentReactions> reactionList = commentReactionService.findAllByCommentId(commentId);
        int likeCounter = 0;
        int dislikeCounter = 0;
        for (CommentReactions reactions : reactionList) {
            if (reactions.getReaction()) {
                likeCounter++;
            } else {
                dislikeCounter++;
            }
        }
        reactionObject.appendField("likeCounter", likeCounter);
        reactionObject.appendField("dislikeCounter", dislikeCounter);

        return reactionObject;
    }
}
