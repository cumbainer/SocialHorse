package ua.socialnetwork.service;

import org.springframework.web.multipart.MultipartFile;
import ua.socialnetwork.dto.UserDto;
import ua.socialnetwork.entity.User;

import java.util.List;

public interface UserService {

    User create(UserDto user);

    User create(UserDto user, MultipartFile userImage);

    User create(UserDto user, MultipartFile userImage, MultipartFile imageBackground);

    User update(User user, MultipartFile userImage);

    User update(User user);

    User update(User user, MultipartFile userImage, MultipartFile imageBackground);
    User returnUserByUsername(String username);

    void delete(int id);

    UserDto readById(int id);

    UserDto readByUsername(String username);

    List<UserDto> getAll();
}
