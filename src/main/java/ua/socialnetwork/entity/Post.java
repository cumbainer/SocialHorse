package ua.socialnetwork.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private int id;

    @Column(name = "body")
    private String body;

    @Column(name = "liked")
    private boolean liked;
    @Column(name = "disliked")
    private boolean disliked;

    @Column(name = "likeCounter")
    private int likeCounter;

    @Column(name = "dislikeCounter")
    private int dislikeCounter;

    @Column(name = "creationDate")
    private String creationDate;

    @Column(name = "editionDate")
    @UpdateTimestamp
    private LocalDateTime editionDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;
    @OneToOne(cascade = CascadeType.ALL)
    private PostImage image;

    public void setImageToPost(PostImage postImage) {
        postImage.setPost(this);
        image = postImage;
    }
    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", body='" + body + '\'' +
                ", liked=" + liked +
                ", disliked=" + disliked +
                ", creationDate=" + creationDate +
                ", editionDate=" + editionDate +
                ", comments=" + comments +
                '}';
    }


}
