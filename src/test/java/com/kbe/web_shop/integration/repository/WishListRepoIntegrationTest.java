package com.kbe.web_shop.integration.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.kbe.web_shop.model.User;
import com.kbe.web_shop.model.WishList;
import com.kbe.web_shop.repository.UserRepo;
import com.kbe.web_shop.repository.WishListRepo;
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
public class WishListRepoIntegrationTest {

    @Autowired
    private WishListRepo wishListRepo;

    @Autowired
    private UserRepo userRepo;

    @Test
    public void testFindAllByUserOrderByCreatedDateDesc() {
        User user = new User();
        userRepo.save(user);

        WishList wishList1 = new WishList();
        wishList1.setUser(user);
        wishList1.setCreatedDate(new Date(System.currentTimeMillis() - 10000));
        wishListRepo.save(wishList1);

        WishList wishList2 = new WishList();
        wishList2.setUser(user);
        wishList2.setCreatedDate(new Date(System.currentTimeMillis() - 5000));
        wishListRepo.save(wishList2);

        WishList wishList3 = new WishList();
        wishList3.setUser(user);
        wishList3.setCreatedDate(new Date());
        wishListRepo.save(wishList3);

        List<WishList> wishLists = wishListRepo.findAllByUserOrderByCreatedDateDesc(user);

        assertNotNull(wishLists);
        assertEquals(3, wishLists.size());
        assertEquals(wishList3, wishLists.get(0));
        assertEquals(wishList2, wishLists.get(1));
        assertEquals(wishList1, wishLists.get(2));
    }
}
