package com.kbe.web_shop.unit.service;

import com.kbe.web_shop.config.constants.Role;
import com.kbe.web_shop.dto.response.ResponseDto;
import com.kbe.web_shop.dto.user.SignInDto;
import com.kbe.web_shop.dto.user.SignInResponseDto;
import com.kbe.web_shop.dto.user.SignUpDto;
import com.kbe.web_shop.exception.AuthenticationFailException;
import com.kbe.web_shop.exception.CustomException;
import com.kbe.web_shop.model.AuthenticationToken;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.producer.AddressProducer;
import com.kbe.web_shop.producer.AuthenticationProducer;
import com.kbe.web_shop.repository.UserRepo;
import com.kbe.web_shop.service.AuthenticationService;
import com.kbe.web_shop.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceUnitTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepo userRepository;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private AddressService addressService;

    @Mock
    private AddressProducer addressProducer;

    @Mock
    private AuthenticationProducer authenticationProducer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignUp() {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setFirstName("John");
        signUpDto.setLastName("Doe");
        signUpDto.setEmail("john.doe@example.com");
        signUpDto.setPassword("password");
        signUpDto.setRole(Role.customer);

        when(userRepository.findByEmail(signUpDto.getEmail())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(authenticationService.getToken(any(User.class))).thenReturn(new AuthenticationToken());

        ResponseDto response = userService.signUp(signUpDto);

        assertNotNull(response);
        assertEquals("success", response.getStatus());
    }

    @Test
    public void testSignUpUserAlreadyExists() {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setFirstName("John");
        signUpDto.setLastName("Doe");
        signUpDto.setEmail("john.doe@example.com");
        signUpDto.setPassword("password");
        signUpDto.setRole(Role.customer);

        when(userRepository.findByEmail(signUpDto.getEmail())).thenReturn(new User());

        assertThrows(CustomException.class, () -> userService.signUp(signUpDto));
    }

    @Test
    public void testSignIn() throws NoSuchAlgorithmException {
        SignInDto signInDto = new SignInDto("john.doe@example.com", "password");

        User user = new User();
        user.setPassword(hashPassword(signInDto.getPassword()));
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        Role role = Role.customer;
        user.setRole(role);

        when(userRepository.findByEmail(signInDto.getEmail())).thenReturn(user);
        when(authenticationService.getToken(user)).thenReturn(authenticationToken);
        when(authenticationService.getUser(authenticationToken.getToken())).thenReturn(user);

        SignInResponseDto response = userService.signIn(signInDto);

        assertNotNull(response);
        assertEquals("sucess", response.getStatus());
        assertEquals(authenticationToken.getToken(), response.getToken());
        assertEquals(role, response.getRole());
    }

    @Test
    public void testSignInUserNotFound() {
        SignInDto signInDto = new SignInDto("john.doe@example.com", "password");

        when(userRepository.findByEmail(signInDto.getEmail())).thenReturn(null);

        assertThrows(AuthenticationFailException.class, () -> userService.signIn(signInDto));
    }

    @Test
    public void testSignInWrongPassword() throws NoSuchAlgorithmException {
        SignInDto signInDto = new SignInDto("john.doe@example.com", "password");

        User user = new User();
        user.setPassword("wrong_password");

        when(userRepository.findByEmail(signInDto.getEmail())).thenReturn(user);

        assertThrows(AuthenticationFailException.class, () -> userService.signIn(signInDto));
    }

    @Test
    public void testHashPassword() throws NoSuchAlgorithmException {
        String password = "password";
        String hashedPassword = hashPassword(password);

        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword);
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String hash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        return hash;
    }
}

