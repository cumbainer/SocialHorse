package ua.socialnetwork.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.socialnetwork.dto.PostDto;
import ua.socialnetwork.entity.Post;
import ua.socialnetwork.entity.enums.PostAction;
import ua.socialnetwork.repo.UserRepo;
import ua.socialnetwork.security.SecurityUser;
import ua.socialnetwork.service.PostService;
import ua.socialnetwork.service.UserService;

import java.time.LocalDateTime;

@Controller
@AllArgsConstructor
@RequestMapping({"/posts", "/"})
@Slf4j
public class PostController {
    private final UserService userService;
    private final PostService postService;

    private final UserRepo userRepo;


    @GetMapping("/feed")
    public String getAll(@AuthenticationPrincipal SecurityUser authUser, Model model) {

        model.addAttribute("imageIsPresent", authUser.getImages().size() > 0);
        model.addAttribute("posts", postService.getAll());
        model.addAttribute("newPost", new Post());
        model.addAttribute("users", userService.getAll());
        model.addAttribute("posts", postService.postPreparation(userRepo.getById((int)authUser.getId())));

        return "feed";
    }

    @PreAuthorize("hasRole('ROLE_ADMIM') or authentication.principal.username == #username")
    @PostMapping("/new/{username}/")
    public String create(@PathVariable("username") String username, Post post,
                         @RequestParam(value = "postImage", required = false) MultipartFile postImage) {

        post.setUser(userService.returnUserByUsername(username));

        postService.create(post, postImage);
        log.info("From PostController a Post has been created, id: " + post.getId());
        return "redirect:/feed";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.principal.id == @postServiceImpl.readById(#postId).user.id")
    @GetMapping("/{post_id}/update/")
    public String editForm(@PathVariable("post_id") Integer postId, Model model) {

        model.addAttribute("post", postService.readById(postId));

        return "update-post";
    }

    @PreAuthorize("hasRole('ROLE_ADMIM') or #postDto.user.id == authentication.principal.id")
    @PostMapping("/update/")
    public String edit(PostDto postDto, BindingResult result, @RequestParam(value = "postImage", required = false) MultipartFile postImage) {

        if (result.hasErrors()) return "update-post";

        postDto.setEditionDate(LocalDateTime.now());
        postService.update(postDto, postImage);

        return "redirect:/feed";
    }

    @PreAuthorize("hasRole('ROLE_ADMIM') or @postServiceImpl.readById(#postId).user.id == authentication.principal.id")
    @GetMapping("/{post_id}/delete/")
    public String delete(@PathVariable("post_id") Integer postId, @AuthenticationPrincipal SecurityUser authUser) {

        postService.delete(postId);

        return "redirect:/users/" + authUser.getUsername();
    }

    @GetMapping("/{post_id}/like/")
    public String like(@PathVariable("post_id") Integer postId) {
        Post post = postService.returnPostEntityById(postId);

        postService.makeReaction(post, PostAction.LIKE);
        postService.create(post);
        return "redirect:/feed";
    }

    @GetMapping("/{post_id}/dislike/")
    public String dislike(@PathVariable("post_id") Integer postId) {
        Post post = postService.returnPostEntityById(postId);

        postService.makeReaction(post, PostAction.DISLIKE);
        postService.create(post);
        return "redirect:/feed";
    }

}
