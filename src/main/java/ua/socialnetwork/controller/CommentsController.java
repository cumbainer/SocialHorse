package ua.socialnetwork.controller;

import com.nimbusds.jose.shaded.json.JSONArray;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.socialnetwork.entity.Comment;
import ua.socialnetwork.entity.Post;
import ua.socialnetwork.entity.User;
import ua.socialnetwork.service.CommentService;
import ua.socialnetwork.service.PostService;
import ua.socialnetwork.service.UserService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/comment")
@Slf4j
public class CommentsController {
    private final CommentService commentService;
    private final PostService postService;
    private UserService userService;

    @GetMapping("/all/{post_id}")
    public String getAll(@PathVariable("post_id") int post_id, Model model) {
        List<Comment> allComments = commentService.getCommentsByPostId(post_id);
        model.addAttribute("allComments", allComments);
        model.addAttribute("post", postService.readById(post_id));
        model.addAttribute("users", userService.getAll());

        log.info("Comments have been reading");
        return "post-comments";
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public JSONArray createComment(@RequestParam("comment") String text, @RequestParam("post_id") int post_id, Principal principal) {

        JSONArray array = new JSONArray();
        Post post = postService.returnPostEntityById(post_id);
        Comment comment = new Comment();
        comment.setPost(post);
        User user = userService.returnUserByUsername(principal.getName());
        comment.setCreatedBy(user);
        comment.setCreatedDate(LocalDateTime.now());
        comment.setText(text);

        commentService.create(comment);
        array.add(comment.getId());
        array.add(user.getFirstName() + " " + user.getLastName());
        return array;
    }
}
