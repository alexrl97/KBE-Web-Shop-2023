package com.kbe.web_shop.integration.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.kbe.web_shop.model.Address;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.repository.AddressRepo;
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
public class AddressRepoIntegrationTest {

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private UserRepo userRepo;

    @Test
    public void testFindTopByUserOrderByCreatedDateDesc() {
        User user = new User();
        userRepo.save(user);

        Address address1 = new Address();
        address1.setUser(user);
        addressRepo.save(address1);

        Address address2 = new Address();
        address2.setUser(user);
        addressRepo.save(address2);

        Address result = addressRepo.findTopByUserOrderByCreatedDateDesc(user);

        assertNotNull(result);
        assertEquals(address2, result);
    }
}
