package ua.socialnetwork.service;

import org.springframework.web.multipart.MultipartFile;
import ua.socialnetwork.dto.UserDto;
import ua.socialnetwork.entity.User;

import java.util.List;

public interface UserService {

    UserDto create(UserDto user);

    UserDto update(UserDto userDto, MultipartFile userImage);

    UserDto update(UserDto userDto, MultipartFile userImage, MultipartFile imageBackground);

    User returnUserByUsername(String username);

    void delete(int id);

    UserDto readById(int id);

    UserDto readByUsername(String username);

    List<UserDto> getAll();
}
