package com.kbe.web_shop.integration.service;

import com.kbe.web_shop.config.constants.Role;
import com.kbe.web_shop.exception.AuthenticationFailException;
import com.kbe.web_shop.model.AuthenticationToken;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.repository.UserRepo;
import com.kbe.web_shop.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AuthenticationServiceIntegrationTest {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepo userRepo;


    @Test
    public void testSaveAndRetrieveToken() {
        User user = new User();
        User savedUser = userRepo.save(user);

        AuthenticationToken authenticationToken = new AuthenticationToken(savedUser);
        authenticationService.saveConfirmationToken(authenticationToken);

        AuthenticationToken retrievedToken = authenticationService.getToken(savedUser);

        assertNotNull(retrievedToken);
        assertEquals(savedUser, retrievedToken.getUser());
    }

    @Test
    public void testAuthenticateWithValidToken() {
        User user = new User();
        User savedUser = userRepo.save(user);

        AuthenticationToken authenticationToken = new AuthenticationToken(savedUser);
        authenticationService.saveConfirmationToken(authenticationToken);

        AuthenticationToken retrievedToken = authenticationService.getToken(savedUser);

        assertDoesNotThrow(() -> authenticationService.authenticate(retrievedToken.getToken()));
    }

    @Test
    public void testAuthenticateWithInvalidToken() {
        assertThrows(AuthenticationFailException.class, () -> authenticationService.authenticate("invalidToken"));
    }

    @Test
    public void testHasEditPermissionWithStorehouseRole() {
        User user = new User();
        user.setRole(Role.storehouse);

        User savedUser = userRepo.save(user);

        AuthenticationToken authenticationToken = new AuthenticationToken(savedUser);
        authenticationService.saveConfirmationToken(authenticationToken);
        AuthenticationToken retrievedToken = authenticationService.getToken(savedUser);

        boolean hasPermission = authenticationService.hasEditPermission(retrievedToken.getToken());
        assertTrue(hasPermission);
    }

    @Test
    public void testHasEditPermissionWithCustomerRole() {
        User user = new User();
        user.setRole(Role.customer);

        User savedUser = userRepo.save(user);

        AuthenticationToken authenticationToken = new AuthenticationToken(savedUser);
        authenticationService.saveConfirmationToken(authenticationToken);
        AuthenticationToken retrievedToken = authenticationService.getToken(savedUser);

        boolean hasPermission = authenticationService.hasEditPermission(retrievedToken.getToken());
        assertFalse(hasPermission);
    }
}
