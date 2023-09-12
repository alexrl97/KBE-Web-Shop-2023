package com.kbe.web_shop.integration.repository;
import static org.junit.jupiter.api.Assertions.*;

import com.kbe.web_shop.model.Cart;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.repository.CartRepo;
import com.kbe.web_shop.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
public class CartRepoIntegrationTest {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private UserRepo userRepo;

    @Test
    public void testFindAllByUserOrderByCreatedDateDesc() {

        User user = new User();
        userRepo.save(user);

        Cart cart1 = new Cart();
        cart1.setUser(user);
        cart1.setCreatedDate(new Date(System.currentTimeMillis() - 10000));
        cartRepo.save(cart1);

        Cart cart2 = new Cart();
        cart2.setUser(user);
        cart2.setCreatedDate(new Date(System.currentTimeMillis() - 5000));
        cartRepo.save(cart2);

        Cart cart3 = new Cart();
        cart3.setUser(user);
        cart3.setCreatedDate(new Date());
        cartRepo.save(cart3);


        List<Cart> carts = cartRepo.findAllByUserOrderByCreatedDateDesc(user);


        assertNotNull(carts);
        assertEquals(3, carts.size());
        assertEquals(cart3, carts.get(0));
        assertEquals(cart2, carts.get(1));
        assertEquals(cart1, carts.get(2));
    }
}