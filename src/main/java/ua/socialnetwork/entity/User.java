package ua.socialnetwork.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.socialnetwork.entity.enums.Gender;
import ua.socialnetwork.entity.enums.UserRole;
import ua.socialnetwork.serializer.UserSerializer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@JsonSerialize(using = UserSerializer.class)
@Getter
@Setter
@NoArgsConstructor
@Entity
@EqualsAndHashCode
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "age")
    private int age;

    @Column(name = "bio")
    private String bio;

    @Column(name = "country")
    private String country;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "employed")
    private boolean employed;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "banned")
    private boolean banned;

    @Column(name = "creationDate")
    private LocalDateTime creationDate;

    @Column(name = "editionDate")
    private LocalDateTime editionDate;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @OneToMany(mappedBy = "sender", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Friend> sentRequest;

    @OneToMany(mappedBy = "receiver", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Friend> receivedRequests;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CommentReactions> reactions;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
            mappedBy = "user")
    private List<UserImage> images = new ArrayList<>();

    public void setProfileImageToUser(UserImage image) {
        image.setUser(this);
        images.add(image);
    }

    public void setBackgroundImageToUser(UserImage image){
        image.setUser(this);
        if(this.getImages() != null && this.getImages().size() >= 1){
            images.add(0, image);
        }
        images.add(image);
    }
}
