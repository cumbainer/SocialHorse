package ua.socialnetwork.entity;


import jakarta.persistence.*;
import lombok.*;


@Entity
//@Getter
//@Setter
@Data
@NoArgsConstructor
@Table(name = "images")
@EqualsAndHashCode
public class UserImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "originalFileName")
    private String originalFileName;

    @Column(name = "size")
    private Long size;


    @Column(name = "contentType")
    private String contentType;

    @Lob
    private byte[] bytes;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
}
