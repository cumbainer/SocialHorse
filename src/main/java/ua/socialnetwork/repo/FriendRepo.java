package ua.socialnetwork.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.socialnetwork.entity.Friend;

public interface FriendRepo extends JpaRepository<Friend, Integer> {
    Friend findFriendByReceiver_UsernameAndSender_Username(String receiverUsername, String senderUsername);
}
