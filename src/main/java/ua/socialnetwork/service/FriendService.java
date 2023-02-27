package ua.socialnetwork.service;

import ua.socialnetwork.dto.FriendDto;
import ua.socialnetwork.entity.Friend;

public interface FriendService {

//    Friend create(Friend friendDto, int senderId, int receiverId);
    Friend create(FriendDto friendDto, String senderUsername, String receiverUsername);

    void removeFromFriends(int friendEntityId);

    FriendDto getFriendByReceiverUsername(String receiverUsername, String senderUsername);
}
