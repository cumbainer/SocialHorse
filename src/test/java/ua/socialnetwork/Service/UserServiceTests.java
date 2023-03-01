package ua.socialnetwork.Service;


import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.socialnetwork.dto.UserDto;
import ua.socialnetwork.entity.User;
import ua.socialnetwork.exception.NullEntityReferenceException;
import ua.socialnetwork.repo.UserRepo;
import ua.socialnetwork.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTests {
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private UserRepo userRepository;

    private User validUser;
    private User invalidUser;

    private UserDto validUserDto;
    private UserDto invalidUserDto;


    @BeforeEach
    void init(){
        validUser = new User();
        validUser.setFirstName("Firstname");
        validUser.setLastName("Lastname");
        validUser.setEmail("email@mail.com");
        validUser.setPassword("Qwerty!12");
        validUser.setUsername("username");
        validUser.setId(111);
        validUserDto = modelMapper.map(validUser, UserDto.class);

        invalidUser = new User();
        invalidUser.setFirstName("First-name");
        invalidUser.setLastName("Last-name");
        invalidUser.setEmail("no email");
        invalidUser.setPassword("");
        invalidUser.setUsername("");
        invalidUser.setId(222);
        invalidUserDto = modelMapper.map(invalidUser, UserDto.class);
    }



    @BeforeEach
    void setUp(){
        when(userRepository.findById((int) validUser.getId())).thenReturn(Optional.of(validUser));
        when(userRepository.findById((int) invalidUser.getId())).thenReturn(Optional.of(invalidUser));
    }

    @Test
    @DisplayName("Create new valid user")
    public void createValidUserTest(){
        given(userRepository.save(validUser)).willReturn(validUser);
        User addedUser  =  userService.create(validUserDto);
        verify(userRepository).save(any(User.class));
        Assertions.assertEquals(validUserDto,addedUser);
    }

    @Test
    @DisplayName("Create invalid user")
    public void creteInvalidUserTest(){
        Mockito.when(userRepository.save(invalidUser)).thenThrow(new IllegalArgumentException());
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->userService.create(invalidUserDto));
    }

    @Test
    @DisplayName("Create pass null value")
    public void createNullUserTest(){
        Mockito.when(userRepository.save(null)).thenThrow(new IllegalArgumentException());
        Assertions.assertThrows(NullPointerException.class,
                ()->userService.create(null));
    }

    @Test
    @DisplayName("Delete existing user")
    public void deleteExistingUserTest(){
        userService.delete((int) validUser.getId());
        Mockito.verify(userRepository,Mockito.times(1)).delete(validUser);
    }

    @Test
    @DisplayName("Delete non existing user")
    public void deleteNonExistingUserTest(){
        int randomId = 342;
        given(userRepository.findById(randomId)).willReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class,
                ()->userService.delete(randomId));
        Mockito.verify(userRepository,Mockito.times(0)).delete(validUser);
    }

    @Test
    @DisplayName("Update valid user")
    public void updateValidUserTest(){
        String newName = "Newfirstname";
        validUser.setFirstName(newName);
        given(userRepository.save(validUser)).willReturn(validUser);
        String nameAfter = userService.update(validUser).getFirstName();
        Assertions.assertEquals(nameAfter, newName);
        Mockito.verify(userRepository,Mockito.times(1)).save(validUser);
    }

    @Test
    @DisplayName("Update inValid user")
    public void updateNullUserTest(){
        Assertions.assertThrows(NullEntityReferenceException.class,
                ()->userService.update(null));
    }

    @Test
    @DisplayName("Get all existing users test")
    public void getAllUsersTest(){
        List<User> allUsers= new ArrayList<>();
        allUsers.add(validUser);

        given(userRepository.findAll()).willReturn(allUsers);
        Assertions.assertEquals(allUsers,userService.getAll());
    }

}
