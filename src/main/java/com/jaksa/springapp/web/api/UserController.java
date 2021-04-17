package com.jaksa.springapp.web.api;

import com.jaksa.springapp.datatransfer.UserDTO;
import com.jaksa.springapp.datamodels.User;
import com.jaksa.springapp.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("user/test1")
    public void testt() {
        registerUserAccount(new UserDTO("admin1", "haslo123", "haslo122", "test@test.com"));
    }

    @PostMapping("/user/registration")
    public User registerUserAccount(@Valid @RequestBody UserDTO userDto) {
        return userService.registerNewUserAccount(userDto);
    }
}
