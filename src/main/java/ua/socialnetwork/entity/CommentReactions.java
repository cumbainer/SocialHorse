package ua.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "commentReactions")
public class CommentReactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reaction_id")
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "reaction")
    private Boolean reaction;
}
