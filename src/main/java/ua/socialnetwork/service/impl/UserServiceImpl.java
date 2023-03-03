package ua.socialnetwork.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.socialnetwork.dto.UserDto;
import ua.socialnetwork.entity.User;
import ua.socialnetwork.entity.UserImage;
import ua.socialnetwork.entity.enums.UserRole;
import ua.socialnetwork.exception.NullEntityReferenceException;
import ua.socialnetwork.exception.UserAlreadyExistsException;
import ua.socialnetwork.repo.UserRepo;
import ua.socialnetwork.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final PasswordEncoder encoder;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    @Override
    public User create(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);

        if (ifUsernameExists(user.getUsername())) {
            throw new UserAlreadyExistsException("There already is an account with username: " + user.getUsername());
        }

        log.info("New account was created with username: " + user.getUsername());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setCreationDate(LocalDateTime.now());
        user.setRole(UserRole.USER);
        return userRepo.save(user);
    }

    @Override
    public User create(UserDto userDto, MultipartFile userImage) {
        User user = modelMapper.map(userDto, User.class);
        UserImage image;

        if (ifUsernameExists(user.getUsername())) {

            throw new UserAlreadyExistsException("There already is an account with username: " + user.getUsername());
        } else if (userImage.getSize() != 0) {
            image = toImageEntity(userImage);
            user.setProfileImageToUser(image);
        }

        log.info("New account was created with username: " + user.getUsername());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(UserRole.USER);
        user.setCreationDate(LocalDateTime.now());
        return userRepo.save(user);

    }

    @Override
    public User create(UserDto userDto, MultipartFile userImage, MultipartFile imageBackground) {
        User user = modelMapper.map(userDto, User.class);
        UserImage image;
        UserImage image2;

        if (ifUsernameExists(user.getUsername())) {
            throw new UserAlreadyExistsException("There already is an account with username: " + user.getUsername());
        } else if (userImage.getSize() != 0) {
            image = toImageEntity(userImage);
            user.setProfileImageToUser(image);
        } else if (imageBackground.getSize() != 0) {
            image2 = toImageEntity(imageBackground);
            user.setProfileImageToUser(image2);
        }

        log.info("New account was created with username: " + user.getUsername());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(UserRole.USER);
        user.setCreationDate(LocalDateTime.now());
        return userRepo.save(user);
    }

    @Override
    public User update(User user, MultipartFile userImage) {
        UserImage image;

        if (user != null) {
            if (userImage.getSize() != 0) {
                image = toImageEntity(userImage);
                user.setProfileImageToUser(image);
            }

            log.info("User with username: " + user.getUsername() + "has been updated");
            user.setRole(UserRole.USER);
            user.setPassword(encoder.encode(user.getPassword()));
            user.setEditionDate(LocalDateTime.now());
            user.setCreationDate(user.getCreationDate());
            return userRepo.save(user);

        }
        throw new NullEntityReferenceException("User can not be null");
    }

    @Override
    public User update(User user) {

        if (user != null) {
            user.setRole(UserRole.USER);
            user.setPassword(encoder.encode(user.getPassword()));
            user.setCreationDate(user.getCreationDate());
            user.setEditionDate(LocalDateTime.now());
            return userRepo.save(user);

        }
        throw new NullEntityReferenceException("User can not be null");
    }

    @Override
    public User update(User user, MultipartFile userImage, MultipartFile imageBackground) {

        UserImage image;
        UserImage image2;
        if (user != null) {
            if (userImage.getSize() != 0) {
                image = toImageEntity(userImage);
                user.setProfileImageToUser(image);
            }
            if (userImage.getSize() != 0) {
                image2 = toImageEntity(imageBackground);
                user.setProfileImageToUser(image2);
            }

            user.setRole(UserRole.USER);
            user.setPassword(encoder.encode(user.getPassword()));
            user.setCreationDate(user.getCreationDate());
            user.setEditionDate(LocalDateTime.now());
            return userRepo.save(user);
        }
        throw new NullEntityReferenceException("User can not be null");
    }

    @Override
    public User returnUserByUsername(String username) {
        return userRepo.findUserByUsername(username).orElseThrow(() ->
                new EntityNotFoundException("User with username: " + username + "not found"));
    }

    @Override
    public void delete(int id) {
        if (id != 0) {
            userRepo.deleteById(id);
            log.info("An user with id: " + id + " was deleted");
        }
    }

    @Override
    public UserDto readById(int id) {
        return userRepo.findById(id).map(user -> modelMapper.map(user, UserDto.class)).orElseThrow(() ->
                new EntityNotFoundException("User with id: " + id + "not found"));
    }

    @Override
    public UserDto readByUsername(String username) {
        return userRepo.findUserByUsername(username).map(user -> modelMapper.map(user, UserDto.class)).orElseThrow(() ->
                new EntityNotFoundException("User with username: " + username + "not found"));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepo.findAll().stream().map(user -> modelMapper.map(user, UserDto.class)).toList();
    }

    private boolean ifUsernameExists(String username) {
        Optional<User> user = userRepo.findUserByUsername(username);

        User ifUser = user.orElse(null);
        return ifUser != null;
    }

    @SneakyThrows
    private UserImage toImageEntity(MultipartFile userImage) {
        UserImage image = new UserImage();
        image.setName(userImage.getName());
        image.setOriginalFileName(userImage.getOriginalFilename());
        image.setContentType(userImage.getContentType());
        image.setSize(userImage.getSize());
        image.setBytes(userImage.getBytes());

        return image;
    }
}
