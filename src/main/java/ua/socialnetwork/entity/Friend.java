package ua.socialnetwork.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.socialnetwork.entity.enums.FriendStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "friends")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne()
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne()
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private FriendStatus status;

    @Column(name = "sendingRequestDate")
    private LocalDateTime sendingRequestDate;
}
