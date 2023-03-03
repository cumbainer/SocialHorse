package ua.socialnetwork.dto;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ua.socialnetwork.entity.Friend;
import ua.socialnetwork.entity.Post;
import ua.socialnetwork.entity.UserImage;
import ua.socialnetwork.entity.enums.Gender;
import ua.socialnetwork.entity.enums.UserRole;
import ua.socialnetwork.serializer.UserSerializer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDto {
    private long id;

//    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name can contain only lathin symbols and nothing more")
    private String firstName;

//    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name can contain only lathin symbols and nothing more")
    private String lastName;

//    @Pattern(regexp = "^[a-zA-Z]+$", message = "Username can contain only lathin symbols and nothing more")
    private String username;

    @Email(message = "Should be email format")
    private String email;

//    @Pattern(regexp = "^(?=.*\\d).{4,}$", message = "Can contain only letters and numbers. Can not be less that 5 symbols")
    private String password;

    private int age;
    private String bio;
    private Gender gender;
    private String country;
    private UserRole role;
    private LocalDateTime editionDate;
    private boolean employed;
    private List<Friend> sentRequest;
    private List<Friend> receivedRequests;
    private List<Post> posts;
    private List<UserImage> images = new ArrayList<>();
}
