package ua.socialnetwork.controller;

import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.socialnetwork.entity.CommentReactions;
import ua.socialnetwork.entity.User;
import ua.socialnetwork.repo.UserRepo;
import ua.socialnetwork.security.SecurityUser;
import ua.socialnetwork.service.CommentReactionService;
import ua.socialnetwork.service.CommentService;

import java.util.List;

@Controller
@RequestMapping("/reaction")
@AllArgsConstructor
public class ReactionController {
    private final CommentReactionService commentReactionService;
    private final CommentService commentService;
    private final UserRepo userRepo;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject setReaction(@RequestParam("comment_id") int comment_id,
                                 @RequestParam("reaction") String reaction, @AuthenticationPrincipal SecurityUser authUser) {

        JSONObject reactionObject = new JSONObject();
        User user = userRepo.getReferenceById((int) authUser.getId());
        CommentReactions commentReaction = commentReactionService.findCommentReactionByCommentIdAndUser(comment_id, user);

        if (commentReaction == null) {
            commentReaction = new CommentReactions();
            commentReaction.setComment(commentService.readById(comment_id));
            commentReaction.setUser(user);
        }

        boolean resultReaction = (reaction.equals("like"));
        if ((null == commentReaction.getReaction()) || (resultReaction != commentReaction.getReaction())) {
            commentReaction.setReaction(resultReaction);
        }
        commentReactionService.save(commentReaction);

        List<CommentReactions> reactionList = commentReactionService.findAllByCommentId(comment_id);
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
