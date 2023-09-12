package com.kbe.web_shop.integration.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.kbe.web_shop.model.AuthenticationToken;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.repository.TokenRepo;
import com.kbe.web_shop.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
public class TokenRepoIntegrationTest {

    @Autowired
    private TokenRepo tokenRepo;

    @Autowired
    private UserRepo userRepo;

    @Test
    public void testFindByUser() {
        User user = new User();
        userRepo.save(user);

        AuthenticationToken token = new AuthenticationToken();
        token.setUser(user);
        token.setToken("testToken123");
        tokenRepo.save(token);

        AuthenticationToken foundToken = tokenRepo.findByUser(user);

        assertNotNull(foundToken);
        assertEquals(token, foundToken);
    }

    @Test
    public void testFindByToken() {
        User user = new User();
        userRepo.save(user);

        AuthenticationToken token = new AuthenticationToken();
        token.setUser(user);
        token.setToken("testToken123");
        tokenRepo.save(token);

        AuthenticationToken foundToken = tokenRepo.findByToken("testToken123");

        assertNotNull(foundToken);
        assertEquals(token, foundToken);
    }
}