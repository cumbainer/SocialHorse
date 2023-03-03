package ua.socialnetwork.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ua.socialnetwork.dto.FriendDto;
import ua.socialnetwork.entity.Friend;
import ua.socialnetwork.entity.User;
import ua.socialnetwork.entity.enums.FriendStatus;
import ua.socialnetwork.repo.FriendRepo;
import ua.socialnetwork.service.FriendService;
import ua.socialnetwork.service.UserService;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@Slf4j
@AllArgsConstructor
public class FriendServiceImpl implements FriendService {
    private FriendRepo friendRepo;
    private UserService userService;
    private ModelMapper modelMapper;

    @Override
    public FriendDto create(FriendDto friendDto, String senderUsername, String receiverUsername) {
        Friend friend = modelMapper.map(friendDto, Friend.class);

        User sender = userService.returnUserByUsername(senderUsername);
        User receiver = userService.returnUserByUsername(receiverUsername);

        friend.setSender(sender);
        friend.setReceiver(receiver);

        receiver.getReceivedRequests().add(friend);
        sender.getSentRequest().add(friend);
        friend.setStatus(FriendStatus.FRIEND);
        friend.setSendingRequestDate(LocalDateTime.now());
        log.info("A friendship has created between [sender: " + sender.getId() + ", receiver: " + receiver.getId() + ']');
        friendRepo.save(friend);
        return friendDto;
    }

    @Override
    public void removeFromFriends(int friendEntityId) {
        Friend friend = friendRepo.findById(friendEntityId).orElseThrow(
                () -> new NoSuchElementException("\"Caught an NoSuchElementException exception while deleting friend with id: \" + friendEntityId"));
        friendRepo.delete(friend);
    }

    @Override
    public FriendDto getFriendByReceiverUsername(String receiverUsername, String senderUsername) {
        try {
            Friend friend = friendRepo.findFriendByReceiver_UsernameAndSender_Username(receiverUsername, senderUsername);
            return modelMapper.map(friend, FriendDto.class);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public FriendDto getById(int friendId) {
        return friendRepo.findById(friendId).map(friend -> modelMapper.map(friend, FriendDto.class)).orElseThrow(
                () -> new NoSuchElementException("\"Caught an NoSuchElementException exception while deleting friend with id: \" + friendEntityId"));
    }
}
