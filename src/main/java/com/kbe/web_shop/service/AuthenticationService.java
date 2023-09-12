package com.kbe.web_shop.service;

import com.kbe.web_shop.config.constants.Role;
import com.kbe.web_shop.exception.AuthenticationFailException;
import com.kbe.web_shop.model.AuthenticationToken;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.repository.TokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthenticationService {

    @Autowired
    TokenRepo tokenRepo;

    public void saveConfirmationToken(AuthenticationToken authenticationToken) {
        tokenRepo.save(authenticationToken);
    }

    public AuthenticationToken getToken(User user) {
        return tokenRepo.findByUser(user);
    }

    public User getUser(String token) {
        final AuthenticationToken authenticationToken = tokenRepo.findByToken(token);
        if(Objects.isNull(authenticationToken)) {
            return null;
        }
        return authenticationToken.getUser();
    }

    public void authenticate(String token) throws AuthenticationFailException {
        if(Objects.isNull(token)) {
            throw new AuthenticationFailException("token not present");
        }
        if(Objects.isNull(getUser(token))) {
            throw new AuthenticationFailException("token not valid");
        }
    }

    public boolean hasEditPermission(String token){
        return getUser(token).getRole().equals(Role.storehouse);
    }


}
