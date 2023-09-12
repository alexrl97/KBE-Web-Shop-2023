package com.kbe.web_shop.integration.service;

import com.kbe.web_shop.config.constants.Role;
import com.kbe.web_shop.dto.product.ProductDto;
import com.kbe.web_shop.model.*;
import com.kbe.web_shop.repository.CategoryRepo;
import com.kbe.web_shop.repository.ProductRepo;
import com.kbe.web_shop.repository.UserRepo;
import com.kbe.web_shop.repository.WishListRepo;
import com.kbe.web_shop.service.ProductService;
import com.kbe.web_shop.service.WishListService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WishListServiceIntegrationTest {

    @Autowired
    private WishListService wishListService;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private WishListRepo wishListRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    private User user;

    private Product product;

    @Test
    public void testCreateAndGetWishList() {
        user = newTestUser();
        product = newTestProduct();

        WishList wishList = new WishList(user, product);
        wishListService.createWishlist(wishList);

        List<WishList> wishListItems = wishListService.getWishListForUser(user);

        assertNotNull(wishListItems);
        assertEquals(1, wishListItems.size());
        assertEquals(product.getName(), wishListItems.get(0).getProduct().getName());
    }

    @Test
    public void testDeleteWishListItemForUser() {
        user = newTestUser();
        product = newTestProduct();

        WishList wishList = new WishList(user, product);
        wishListService.createWishlist(wishList);

        List<WishList> wishListItems = wishListService.getWishListForUser(user);
        wishList = wishListRepo.findAllByUserOrderByCreatedDateDesc(user).get(0);

        assertEquals(wishListItems.size(), 1);
        wishListService.deleteWishListItem(wishList.getId(), user);
        wishListItems = wishListService.getWishListForUser(user);
        assertEquals(wishListItems.size(), 0);
    }


    private User newTestUser() {
        User user = new User();
        String email = randomString() + "@mail.de";
        user.setEmail(email);
        userRepo.save(user);
        return userRepo.findByEmail(email);
    }

    private Product newTestProduct(){

        Category category = new Category();
        category.setCategoryName("Test Category");
        category.setId(100);
        category = categoryRepo.findFirstByOrderByIdDesc();

        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setDescription("Description 1");
        product1.setImageURL("image1.jpg");
        product1.setPrice(19.99);
        product1.setDeckCardId("12345");
        product1.setRarity("Common");
        product1.setCategory(category);
        productRepo.save(product1);

        List<Product> allProducts = productRepo.findAll();

        return allProducts.stream()
                .filter(p -> p.getName().equals("Product 1"))
                .findFirst()
                .orElse(null);
    }

    private String randomString(){
        Random random = new Random();
        return random.ints(92, 123 + 1)
                .limit(10)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
