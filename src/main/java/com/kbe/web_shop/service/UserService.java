package com.kbe.web_shop.service;

import com.kbe.web_shop.config.constants.MessageStrings;
import com.kbe.web_shop.config.constants.ResponseStatus;
import com.kbe.web_shop.dto.user.StorehouseUserCreateDto;
import com.kbe.web_shop.model.Address;
import com.kbe.web_shop.repository.AddressRepo;
import com.kbe.web_shop.utils.Helper;
import com.kbe.web_shop.config.constants.Role;
import com.kbe.web_shop.dto.ResponseDto;
import com.kbe.web_shop.dto.user.SignInDto;
import com.kbe.web_shop.dto.user.SignInResponseDto;
import com.kbe.web_shop.dto.user.SignUpDto;
import com.kbe.web_shop.exception.AuthenticationFailException;
import com.kbe.web_shop.exception.CustomException;
import com.kbe.web_shop.model.AuthenticationToken;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import static com.kbe.web_shop.config.constants.MessageStrings.USER_CREATED;
import static com.kbe.web_shop.config.constants.MessageStrings.WRONG_PASSWORD;

@Service
public class UserService {

    @Autowired
    UserRepo userRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    AddressService addressService;


    @Transactional
    public ResponseDto signUp(SignUpDto signupDto) {
        if (Helper.notNull(userRepository.findByEmail(signupDto.getEmail()))) {
            throw new CustomException("User already exists");
        }
        // first encrypt the password
        String encryptedPassword = signupDto.getPassword();
        try {
            encryptedPassword = hashPassword(signupDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        User user = new User(signupDto.getFirstName(), signupDto.getLastName(), signupDto.getEmail(), encryptedPassword, signupDto.getRole());
        addressService.createSignUpAdress(signupDto, user);


        User createdUser;
        try {
            // save the User
            createdUser = userRepository.save(user);
            // generate token for user
            final AuthenticationToken authenticationToken = new AuthenticationToken(createdUser);
            // save token in database
            authenticationService.saveConfirmationToken(authenticationToken);
            // success in creating
            return new ResponseDto(ResponseStatus.success.toString(), USER_CREATED);
        } catch (Exception e) {
            // handle signup error
            throw new CustomException(e.getMessage());
        }
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String hash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        return hash;
    }

    public SignInResponseDto signIn(SignInDto signInDto) {
        // find user by email

        User user = userRepository.findByEmail(signInDto.getEmail());

        if (Objects.isNull(user)) {
            throw new AuthenticationFailException("user is not valid");
        }

        // hash the password

        try {
            if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))) {
                throw new AuthenticationFailException(WRONG_PASSWORD);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // compare the password in DB

        // if password match

        AuthenticationToken token = authenticationService.getToken(user);
        Role role = authenticationService.getUser(token.getToken()).getRole();

        // retrive the token

        if (Objects.isNull(token)) {
            throw new CustomException("token is not present");
        }

        return new SignInResponseDto("sucess", token.getToken(), role);

        // return response
    }
/*
    public ResponseDto createStorehouseUser(String token, StorehouseUserCreateDto userCreateDto) throws CustomException, AuthenticationFailException {
        User creatingUser = authenticationService.getUser(token);
        if (!canCrudUser(creatingUser.getRole())) {
            // user can't create new user
            throw  new AuthenticationFailException(MessageStrings.USER_NOT_PERMITTED);
        }
        String encryptedPassword = userCreateDto.getPassword();
        try {
            encryptedPassword = hashPassword(userCreateDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        User user = new User(userCreateDto.getFirstName(), userCreateDto.getLastName(), userCreateDto.getEmail(), encryptedPassword, Role.storehouse);
        User createdUser;
        try {
            createdUser = userRepository.save(user);
            final AuthenticationToken authenticationToken = new AuthenticationToken(createdUser);
            authenticationService.saveConfirmationToken(authenticationToken);
            return new ResponseDto(ResponseStatus.success.toString(), USER_CREATED);
        } catch (Exception e) {
            // handle user creation fail error
            throw new CustomException(e.getMessage());
        }
    }

 */

    boolean canCrudUser(Role role) {
        if (role == Role.storehouse) {
            return true;
        }
        return false;
    }

}