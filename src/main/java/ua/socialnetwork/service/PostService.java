package ua.socialnetwork.service;

import org.springframework.web.multipart.MultipartFile;
import ua.socialnetwork.dto.PostDto;
import ua.socialnetwork.entity.Post;
import ua.socialnetwork.entity.User;
import ua.socialnetwork.entity.enums.PostAction;

import java.util.List;

public interface PostService {
    Post create(Post post);

    PostDto create(PostDto postDto, String username ,MultipartFile postImage);

    PostDto update(PostDto postDto, MultipartFile multipartFile);
    Post returnPostEntityById(Integer postId);

    void delete(int id);

    PostDto readById(int id);

    List<PostDto> getPostsByUserUsername(String username);

    List<PostDto> getAll();

    void makeReaction(Post post, PostAction action);

    List<Object> postPreparation(User user);
    List<Object> postPreparationForUser(User user);
}
