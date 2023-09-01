package com.kbe.web_shop.controller;

import com.kbe.web_shop.dto.response.ResponseDto;
import com.kbe.web_shop.dto.user.SignInDto;
import com.kbe.web_shop.dto.user.SignInResponseDto;
import com.kbe.web_shop.dto.user.SignUpDto;
import com.kbe.web_shop.exception.AuthenticationFailException;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.producer.UserProducer;
import com.kbe.web_shop.repository.UserRepo;
import com.kbe.web_shop.service.AuthenticationService;
import com.kbe.web_shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UserController {

    @Autowired
    UserRepo userRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    UserService userService;

    @Autowired
    UserProducer userProducer;

    @GetMapping("/all")
    public List<User> findAllUser(@RequestParam("token") String token) throws AuthenticationFailException {
        authenticationService.authenticate(token);
        return userRepository.findAll();
    }

    @PostMapping("/signup")
    public void signup(@RequestBody SignUpDto signUpDto) {
        userProducer.sendCreateUserMessage(signUpDto);
    }


    @PostMapping("/signin")
    public SignInResponseDto signIn(@RequestBody SignInDto signInDto) {
        return userService.signIn(signInDto);
    }

}
