package com.kbe.web_shop.integration.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.kbe.web_shop.model.User;
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
public class UserRepoIntegrationTest {

    @Autowired
    private UserRepo userRepo;

    @Test
    public void testFindByEmail() {
        User user = new User();
        user.setEmail("test@example.com");
        userRepo.save(user);

        User foundUser = userRepo.findByEmail("test@example.com");

        assertNotNull(foundUser);
        assertEquals(user, foundUser);
    }
}
