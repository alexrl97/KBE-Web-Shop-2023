package com.kbe.web_shop.unit.controller;

import com.kbe.web_shop.config.constants.Role;
import com.kbe.web_shop.controller.UserController;
import com.kbe.web_shop.dto.user.SignInDto;
import com.kbe.web_shop.dto.user.SignInResponseDto;
import com.kbe.web_shop.dto.user.SignUpDto;
import com.kbe.web_shop.exception.AuthenticationFailException;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.producer.UserProducer;
import com.kbe.web_shop.repository.UserRepo;
import com.kbe.web_shop.service.AuthenticationService;
import com.kbe.web_shop.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserControllerUnitTest {

    @Mock
    private UserRepo userRepository;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserService userService;

    @Mock
    private UserProducer userProducer;

    @InjectMocks
    private UserController userController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAllUserSuccess() throws AuthenticationFailException {
        String token = "validToken";
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());

        when(userRepository.findAll()).thenReturn(userList);

        List<User> response = userController.findAllUser(token);

        assertEquals(userList, response);
    }

    @Test
    public void testSignUp() {
        SignUpDto signUpDto = new SignUpDto();

        userController.signUp(signUpDto);

        verify(userProducer, times(1)).sendCreateUserMessage(signUpDto);
    }

    @Test
    public void testSignIn() {
        SignInDto signInDto = new SignInDto("email", "password");
        SignInResponseDto responseDto = new SignInResponseDto("success", "token", Role.customer);

        when(userService.signIn(signInDto)).thenReturn(responseDto);

        SignInResponseDto response = userController.signIn(signInDto);

        assertEquals(responseDto, response);
    }
}

