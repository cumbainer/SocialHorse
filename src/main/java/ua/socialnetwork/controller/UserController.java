package ua.socialnetwork.controller;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.socialnetwork.dto.FriendDto;
import ua.socialnetwork.dto.UserDto;
import ua.socialnetwork.entity.User;
import ua.socialnetwork.exception.UserAlreadyExistsException;
import ua.socialnetwork.repo.UserRepo;
import ua.socialnetwork.security.SecurityUser;
import ua.socialnetwork.service.FriendService;
import ua.socialnetwork.service.PostService;
import ua.socialnetwork.service.UserService;

@Controller
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final PostService postService;
    private final FriendService friendService;
    private final UserRepo userRepo;



    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("user", new User());

        return "create-user";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) return "create-user";

        try {
            userService.create(userDto);
        } catch (UserAlreadyExistsException ex) {
            log.warn("UserAlreadyExistsException is caught in UserController with mesasge: " + ex.getMessage() + " and" +
                    "cause " + ex.getCause());
            model.addAttribute("message", "An account for that username/email already exists.");
            return "login-page";
        }

        return "redirect:/users/create/continue/" + userDto.getUsername();
    }

    @GetMapping("/{user_id}/update/")
    public String updateForm(@PathVariable("user_id") Integer user_id, Model model) {

        model.addAttribute("user", userService.readById(user_id));

        return "update-user";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.principal.id == #userDto.id")
    @PostMapping("/update")
    public String update(@Valid UserDto userDto, @RequestParam(value = "userImage", required = false) MultipartFile userImage,
                         BindingResult result) {

        if (result.hasErrors()) return "update-user";

        userService.update(userDto, userImage);
        return "redirect:/users/" + userDto.getUsername();
    }


    @GetMapping("/create/continue/{username}")
    public String createSecondaryInfoForm(@PathVariable("username") String username, Model model) {
        User user = userService.returnUserByUsername(username);

        model.addAttribute("user", user);

        return "create-secondaryInfo";
    }

    @PostMapping("/create/continue/{username}/")
    public String createSecondaryInfo(@PathVariable("username") String username, UserDto user,
                                      @RequestParam(value = "userImage", required = false) MultipartFile userImage,
                                      @RequestParam(value = "imageBackground", required = false) MultipartFile imageBackground,
                                      BindingResult result) {

        if (result.hasErrors()) return "create-secondaryInfo";

        UserDto oldUser = userService.readByUsername(username);

        user.setPassword(oldUser.getPassword());
        user.setFirstName(oldUser.getFirstName());
        user.setLastName(oldUser.getLastName());
        user.setUsername(oldUser.getUsername());
        user.setEmail(oldUser.getEmail());

        userService.update(user, userImage, imageBackground);

        return "redirect:/login";
    }

    @GetMapping("/{username}")
    public String getUser(@PathVariable("username") String username, @AuthenticationPrincipal SecurityUser authUser, Model model) {
        User user = userService.returnUserByUsername(username);

        FriendDto friend = friendService.getFriendByReceiverUsername(username, authUser.getUsername());

        model.addAttribute("ifFriend", friend != null);
        model.addAttribute("isAccount", authUser.getUsername().equals(username));
        model.addAttribute("imageIsPresent", authUser.getImages().size() > 0);
        model.addAttribute("posts", postService.postPreparationForUser(userRepo.findById((int)user.getId()).orElse(null)));

        model.addAttribute("user", userService.readByUsername(username));
        model.addAttribute("image", user.getImages());
        model.addAttribute("users", userService.getAll());
        model.addAttribute("size", user.getImages().size());

        return "profile-page";
    }

    @GetMapping("/{username}/friends")
    public String getFriendList(@PathVariable("username") String username, @AuthenticationPrincipal SecurityUser authUser, Model model) {
        UserDto userDto = userService.readByUsername(username);

        model.addAttribute("imageIsPresent", authUser.getImages().size() > 0);
        model.addAttribute("users", userService.getAll());

        model.addAttribute("user", userDto);
        model.addAttribute("image", userDto.getImages());

        model.addAttribute("size", userDto.getImages().size());
        return "friend-list";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.principal.id == #id")
    @GetMapping("/{user_id}/delete")
    public String deleteUser(@PathVariable("user_id") Integer id) {
        userService.delete(id);
        return "redirect:/feed";
    }

}
