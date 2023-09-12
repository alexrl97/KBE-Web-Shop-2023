package com.kbe.web_shop.unit.service;

import com.kbe.web_shop.config.constants.Role;
import com.kbe.web_shop.exception.AuthenticationFailException;
import com.kbe.web_shop.model.AuthenticationToken;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.repository.TokenRepo;
import com.kbe.web_shop.service.AuthenticationService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class AuthenticationServiceUnitTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private TokenRepo tokenRepo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testGetToken() {
        User user = new User();
        AuthenticationToken authenticationToken = new AuthenticationToken();
        when(tokenRepo.findByUser(user)).thenReturn(authenticationToken);

        AuthenticationToken resultToken = authenticationService.getToken(user);

        assertEquals(authenticationToken, resultToken);
    }

    @Test
    public void testGetUserWithValidToken() {
        String token = "validToken";
        User user = new User();
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        when(tokenRepo.findByToken(token)).thenReturn(authenticationToken);

        User resultUser = authenticationService.getUser(token);

        assertEquals(user, resultUser);
    }

    @Test
    public void testGetUserWithInvalidToken() {
        String token = "invalidToken";
        when(tokenRepo.findByToken(token)).thenReturn(null);

        User resultUser = authenticationService.getUser(token);

        assertNull(resultUser);
    }

    @Test
    public void testAuthenticateWithValidToken() {
        String token = "validToken";
        User user = new User();
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        when(tokenRepo.findByToken(token)).thenReturn(authenticationToken);

        assertDoesNotThrow(() -> authenticationService.authenticate(token));
    }

    @Test(expected = AuthenticationFailException.class)
    public void testAuthenticateWithNullToken() throws AuthenticationFailException {
        authenticationService.authenticate(null);
    }

    @Test(expected = AuthenticationFailException.class)
    public void testAuthenticateWithInvalidToken() throws AuthenticationFailException {
        String token = "invalidToken";
        when(tokenRepo.findByToken(token)).thenReturn(null);

        authenticationService.authenticate(token);
    }

    @Test
    public void testHasEditPermissionWithValidToken() {
        String token = "validToken";
        User user = new User();
        user.setRole(Role.storehouse);
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        when(tokenRepo.findByToken(token)).thenReturn(authenticationToken);

        boolean hasPermission = authenticationService.hasEditPermission(token);

        assertTrue(hasPermission);
    }

}

