package ua.socialnetwork.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentReactionDto {

    private int reaction_id;

    private boolean reaction;

    private int user_id;

    private int comment_id;
}
