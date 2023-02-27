package ua.socialnetwork.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ua.socialnetwork.entity.PostImage;
import ua.socialnetwork.entity.UserImage;
import ua.socialnetwork.repo.PostImageRepo;
import ua.socialnetwork.repo.UserImageRepo;

import java.io.ByteArrayInputStream;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final UserImageRepo userImageRepo;
    private final PostImageRepo postImageRepo;

    @GetMapping("/images/{id}")
    private ResponseEntity<?> getUserImage(@PathVariable Integer id) {
        UserImage image = userImageRepo.findById(id).orElse(null);

        return ResponseEntity.ok().contentType(MediaType.valueOf(image.getContentType())).contentLength(image.getSize()).body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }

    @GetMapping("/post/images/{id}")
    private ResponseEntity<?> getPostImage(@PathVariable Integer id) {
        PostImage image = postImageRepo.findById(id).orElse(null);

        return ResponseEntity.ok().contentType(MediaType.valueOf(image.getContentType())).contentLength(image.getSize()).body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }

}
