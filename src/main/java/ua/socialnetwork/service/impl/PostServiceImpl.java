package ua.socialnetwork.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.socialnetwork.dto.PostDto;
import ua.socialnetwork.entity.*;
import ua.socialnetwork.entity.enums.PostAction;
import ua.socialnetwork.repo.CommentReactionRepo;
import ua.socialnetwork.repo.CommentRepo;
import ua.socialnetwork.repo.PostRepo;
import ua.socialnetwork.service.PostService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class PostServiceImpl implements PostService {
    private PostRepo postRepo;
    private ModelMapper modelMapper;
    private CommentRepo commentRepo;
    private CommentReactionRepo commentReactionRepo;

    @Override
    public Post create(Post post) {
        return postRepo.save(post);
    }

    @Override
    public Post create(Post post, MultipartFile postImage) {
        PostImage image;

        if (postImage.getSize() != 0) {
            image = toImageEntity(postImage);
            post.setImageToPost(image);
        }

        log.info("A post with id: " + post.getId() + " was created");

        post.setCreationDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        return postRepo.save(post);
    }

    @Override
    public Post update(PostDto postDto, MultipartFile postImage) {
        PostImage image;
        Post post = modelMapper.map(postDto, Post.class);

        if (postImage.getSize() != 0) {
            image = toImageEntity(postImage);
            post.setImageToPost(image);
        }

        log.info("A post with id: " + post.getId() + " was updated");

        post.setEditionDate(LocalDateTime.now());
        return postRepo.save(post);
    }

    @Override
    public Post returnPostEntityById(Integer postId) {
        return postRepo.findById(postId).orElseThrow(
                () -> new EntityNotFoundException("Post with id: " + postId + "has not been found"));
    }

    @Override
    public PostDto readById(int id) {
        return postRepo.findById(id).map(post -> modelMapper.map(post, PostDto.class)).orElseThrow(
                () -> new EntityNotFoundException("Post with id: " + id + "has not been found"));
    }

    @Override
    public void delete(int id) {
        if (id != 0) {
            postRepo.deleteById(id);
            log.info("A post with id" + id + "has been deleted");
        }
        log.error("An error occurred in Post deletion with id: " + id);
    }

    @Override
    public List<PostDto> getAll() {
        return postRepo.findAll(Sort.by(Sort.Direction.DESC, "creationDate")).stream()
                .map(post -> modelMapper.map(post, PostDto.class)).toList();
    }

    @Override
    public List<PostDto> getPostsByUserUsername(String username) {
        return postRepo.getPostsByUser_Username(username, Sort.by(Sort.Direction.DESC, "id")).stream()
                .map(post -> modelMapper.map(post, PostDto.class)).toList();
    }

    @SneakyThrows
    private PostImage toImageEntity(MultipartFile postImage) {
        PostImage image = new PostImage();
        image.setName(postImage.getName());
        image.setOriginalFileName(postImage.getOriginalFilename());
        image.setContentType(postImage.getContentType());
        image.setSize(postImage.getSize());
        image.setBytes(postImage.getBytes());

        return image;
    }


    //Needed to put like/dislike on a post
    public void makeReaction(Post postDto, PostAction action) {

        int likeCounter = postDto.getLikeCounter();
        int dislikeCounter = postDto.getDislikeCounter();

        switch (action) {
            case LIKE -> {
                postDto.setLiked(true);
                likeCounter++;
                postDto.setLikeCounter(likeCounter);
                if (dislikeCounter != 0) {
                    dislikeCounter--;

                }
                postDto.setDislikeCounter(dislikeCounter);
                postDto.setDisliked(false);
                log.info("Post with id: " + postDto.getId() + " is liked");
            }
            case DISLIKE -> {
                postDto.setDisliked(true);
                dislikeCounter++;
                postDto.setDislikeCounter(dislikeCounter);
                if (likeCounter != 0) {
                    likeCounter--;

                }
                postDto.setLikeCounter(likeCounter);
                postDto.setLiked(false);
                log.info("Post with id: " + postDto.getId() + " is disliked");
            }
        }
    }


    public List<Object> postPreparation(User user) {
        List<Object> dataPosts = new ArrayList<>();

        int likeSum;
        int dislikeSum;
        boolean isLike;
        boolean isDislike;

        List<Post> posts = postRepo.findAll();
        for (Post post : posts) {
            List<Object> itemPost = new ArrayList<>();
            List<Object> itemComments = new ArrayList<>();
            itemPost.add(0, post);

            List<Comment> comments = commentRepo.findAllByPostId((int) post.getId());
            for (Comment comment : comments) {
                List<Object> itemComment = new ArrayList<>();
                itemComment.add(0, comment);

                likeSum = 0;
                dislikeSum = 0;
                isLike = false;
                isDislike = false;

                List<CommentReactions> reactions = commentReactionRepo.findAllByCommentId((int) comment.getId());
                for (CommentReactions reaction : reactions) {
                    if (reaction.getReaction()) {
                        likeSum++;
                    } else {
                        dislikeSum++;
                    }
                    if (reaction.getUser().getId() == user.getId()) {
                        if (reaction.getReaction()) {
                            isLike = true;
                        } else {
                            isDislike = true;
                        }
                    }
                }
                itemComment.add(1, likeSum);
                itemComment.add(2, dislikeSum);
                itemComment.add(3, isLike);
                itemComment.add(4, isDislike);
                itemComments.add(itemComment);
            }
            itemPost.add(1, itemComments);
            dataPosts.add(0, itemPost);
        }
        return dataPosts;
    }
    @Override
    public List<Object> postPreparationForUser(User user) {
        List<Object> dataPosts = new ArrayList<>();

        int likeSum;
        int dislikeSum;
        boolean isLike;
        boolean isDislike;

        List<Post> posts = postRepo.getPostsByUserId((int) user.getId());
        for (Post post : posts) {
            List<Object> itemPost = new ArrayList<>();
            List<Object> itemComments = new ArrayList<>();
            itemPost.add(0, post);

            List<Comment> comments = commentRepo.findAllByPostId((int) post.getId());
            for (Comment comment : comments) {
                List<Object> itemComment = new ArrayList<>();
                itemComment.add(0, comment);

                likeSum = 0;
                dislikeSum = 0;
                isLike = false;
                isDislike = false;

                List<CommentReactions> reactions = commentReactionRepo.findAllByCommentId((int) comment.getId());
                for (CommentReactions reaction : reactions) {
                    if (reaction.getReaction()) {
                        likeSum++;
                    } else {
                        dislikeSum++;
                    }
                    if (reaction.getUser().getId() == user.getId()) {
                        if (reaction.getReaction()) {
                            isLike = true;
                        } else {
                            isDislike = true;
                        }
                    }
                }
                itemComment.add(1, likeSum);
                itemComment.add(2, dislikeSum);
                itemComment.add(3, isLike);
                itemComment.add(4, isDislike);
                itemComments.add(itemComment);
            }
            itemPost.add(1, itemComments);
            dataPosts.add(0, itemPost);
        }
        return dataPosts;
    }

}
