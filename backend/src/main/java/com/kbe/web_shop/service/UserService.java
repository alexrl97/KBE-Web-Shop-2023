package com.kbe.web_shop.service;

import com.kbe.web_shop.config.constants.ResponseStatus;
import com.kbe.web_shop.model.Address;
import com.kbe.web_shop.producer.AddressProducer;
import com.kbe.web_shop.producer.AuthenticationProducer;
import com.kbe.web_shop.utils.Helper;
import com.kbe.web_shop.config.constants.Role;
import com.kbe.web_shop.dto.response.ResponseDto;
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

    @Autowired
    AddressProducer addressProducer;

    @Autowired
    AuthenticationProducer authenticationProducer;


    @Transactional
    public ResponseDto signUp(SignUpDto signupDto) {
        if (Helper.notNull(userRepository.findByEmail(signupDto.getEmail()))) {
            throw new CustomException("User already exists");
        }

        String encryptedPassword = signupDto.getPassword();
        try {
            encryptedPassword = hashPassword(signupDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        User user = new User(signupDto.getFirstName(), signupDto.getLastName(), signupDto.getEmail(), encryptedPassword, signupDto.getRole());

        User createdUser;
        try {
            createdUser = userRepository.save(user);
            final AuthenticationToken authenticationToken = new AuthenticationToken(createdUser);

            //authenticationService.saveConfirmationToken(authenticationToken);
            authenticationProducer.sendCreateAuthenticationTokenMessage(authenticationToken);
            if(signupDto.getRole().equals(Role.customer))
            addressProducer.sendCreateUpdateAddressMessage(addressService.getAddressFromSignUpDto(signupDto, user));

            return new ResponseDto(ResponseStatus.success.toString(), USER_CREATED);
        } catch (Exception e) {
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

        User user = userRepository.findByEmail(signInDto.getEmail());

        if (Objects.isNull(user)) {
            throw new AuthenticationFailException("user is not valid");
        }

        try {
            if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))) {
                throw new AuthenticationFailException(WRONG_PASSWORD);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        AuthenticationToken token = authenticationService.getToken(user);
        Role role = authenticationService.getUser(token.getToken()).getRole();


        if (Objects.isNull(token)) {
            throw new CustomException("token is not present");
        }

        return new SignInResponseDto("sucess", token.getToken(), role);

    }
}