package ua.socialnetwork.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.socialnetwork.dto.FriendDto;
import ua.socialnetwork.service.FriendService;


@AllArgsConstructor
@Controller
@RequestMapping("/friend")
public class FriendController {
    private FriendService friendService;

    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.principal.username == #senderUsername or authentication.principal.username == #receiverUsername")
    @GetMapping("{sender_username}/add/{receiver_username}")
    public String addFriend(@PathVariable("sender_username") String senderUsername,
                            @PathVariable("receiver_username") String receiverUsername) {

        friendService.create(new FriendDto(), senderUsername, receiverUsername);

        return "redirect:/feed";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.principal.id == @friendServiceImpl.getById(#friendId).sender.id or authentication.principal.id == @friendServiceImpl.getById(#friendId).receiver.id")
    @GetMapping("/{friendId}/delete")
    public String deleteFromFriendList(@PathVariable("friendId") Integer friendId) {
        friendService.removeFromFriends(friendId);

        return "redirect:/feed";
    }

}
