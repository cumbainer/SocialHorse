package ua.socialnetwork.controller.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.socialnetwork.dto.UserDto;
import ua.socialnetwork.entity.User;
import ua.socialnetwork.service.UserService;


//Super early version just for test
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserRestController {
    private UserService userService;

    @GetMapping(value = "/get/{username}", produces = "application/json")
    public ResponseEntity<User> getInfoByUsername(@PathVariable("username") String username){

        HttpHeaders headers = new HttpHeaders();
        headers.set("Access-Control-Allow-Origin", "*");

        User user = userService.returnUserByUsername(username);

        return new ResponseEntity<>(user, headers, HttpStatus.OK);
    }
}
