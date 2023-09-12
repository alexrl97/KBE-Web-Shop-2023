package com.kbe.web_shop.integration.controller;

import com.kbe.web_shop.config.constants.Role;
import com.kbe.web_shop.controller.UserController;
import com.kbe.web_shop.dto.user.SignInDto;
import com.kbe.web_shop.dto.user.SignUpDto;
import com.kbe.web_shop.model.AuthenticationToken;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.repository.UserRepo;
import com.kbe.web_shop.service.AuthenticationService;
import com.kbe.web_shop.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserControllerIntegrationTest {

    @Autowired
    private UserController userController;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthenticationService authenticationService;

    private String email;


    @Test
    public void testSignUp() throws InterruptedException {
        SignUpDto signUpDto1 = new SignUpDto();
        email = getRandomEmail();
        signUpDto1.setFirstName("John");
        signUpDto1.setLastName("Doe");
        signUpDto1.setEmail(email);
        signUpDto1.setPassword("password");
        signUpDto1.setRole(Role.customer);

        userController.signUp(signUpDto1);

        Thread.sleep(500);

        User user = userRepo.findByEmail(email);
        assertNotNull(user);
    }

    @Test
    public void testFindAllUser(){
        User user = new User();
        user = userRepo.save(user);
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);

        assertNotNull(userController.findAllUser(authenticationToken.getToken()));
    }

    private String getRandomEmail(){
        Random random = new Random();
        String generatedString = random.ints(92, 123 + 1)
                .limit(10)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString + "@mail.de";
    }
}
