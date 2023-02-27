package ua.socialnetwork.dto;

import lombok.Data;
import ua.socialnetwork.entity.enums.FriendStatus;

import java.time.LocalDateTime;

@Data
public class FriendDto {
    private int id;
    private UserDto sender;
    private UserDto receiver;
    private FriendStatus status;
    private LocalDateTime sendingRequestDate;
}
