package com.kbe.web_shop.integration.service;

import com.kbe.web_shop.config.constants.Role;
import com.kbe.web_shop.dto.user.SignInDto;
import com.kbe.web_shop.dto.user.SignInResponseDto;
import com.kbe.web_shop.dto.user.SignUpDto;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.repository.UserRepo;
import com.kbe.web_shop.service.UserService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    private String email;


    @Test
    @Order(2)
    public void testSignUpWithDuplicateEmail() {
        SignUpDto signUpDto1 = new SignUpDto();
        email = getRandomEmail();
        signUpDto1.setFirstName("John");
        signUpDto1.setLastName("Doe");
        signUpDto1.setEmail(email);
        signUpDto1.setPassword("password");
        signUpDto1.setRole(Role.customer);

        userService.signUp(signUpDto1);

        SignUpDto signUpDto2 = new SignUpDto();
        signUpDto2.setFirstName("Jane");
        signUpDto2.setLastName("Doe");
        signUpDto2.setEmail(email); // Duplicate email
        signUpDto2.setPassword("anotherpassword");
        signUpDto2.setRole(Role.customer);

        assertThrows(Exception.class, () -> userService.signUp(signUpDto2));

        User user = userRepo.findByEmail(email);
        assertNotNull(user);
    }

    @Test
    @Order(3)
    public void testSignInWithInvalidCredentials() {
        SignUpDto signUpDto = new SignUpDto();
        email = getRandomEmail();
        signUpDto.setFirstName("John");
        signUpDto.setLastName("Doe");
        signUpDto.setEmail(email);
        signUpDto.setPassword("password");
        signUpDto.setRole(Role.customer);

        userService.signUp(signUpDto);

        SignInDto signInDto = new SignInDto(email, "wrongpassword");

        assertThrows(Exception.class, () -> userService.signIn(signInDto));

        User user = userRepo.findByEmail(email);
        assertNotNull(user);
    }

    @Test
    @Order(4)
    public void testPasswordHash(){
        SignUpDto signUpDto = new SignUpDto();
        email = getRandomEmail();
        signUpDto.setFirstName("John");
        signUpDto.setLastName("Doe");
        signUpDto.setEmail(email);
        signUpDto.setPassword("password");
        signUpDto.setRole(Role.customer);

        userService.signUp(signUpDto);

        User user = userRepo.findByEmail(email);
        assertNotEquals(user.getPassword(), signUpDto.getPassword());
        assertEquals(user.getPassword().length(),32);
        assertEquals(signUpDto.getPassword().length(),8);
    }


    @Test
    @Order(1)
    public void testSignUpAndSignIn() throws InterruptedException {
        SignUpDto signUpDto = new SignUpDto();
        email = getRandomEmail();
        signUpDto.setFirstName("John");
        signUpDto.setLastName("Doe");
        signUpDto.setEmail(email);
        signUpDto.setPassword("password");
        signUpDto.setRole(Role.customer);

        userService.signUp(signUpDto);

        User user = userRepo.findByEmail(email);
        assertNotNull(user);

        SignInDto signInDto = new SignInDto(email, signUpDto.getPassword());
        Thread.sleep(500);
        SignInResponseDto responseDto = userService.signIn(signInDto);

        assertNotNull(responseDto);
        assertEquals(Role.customer, responseDto.getRole());
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

