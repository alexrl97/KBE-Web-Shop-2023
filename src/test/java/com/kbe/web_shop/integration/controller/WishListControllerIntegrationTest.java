package com.kbe.web_shop.integration.controller;

import com.kbe.web_shop.common.ApiResponse;
import com.kbe.web_shop.controller.WishListController;
import com.kbe.web_shop.model.*;
import com.kbe.web_shop.repository.CategoryRepo;
import com.kbe.web_shop.repository.ProductRepo;
import com.kbe.web_shop.repository.UserRepo;
import com.kbe.web_shop.repository.WishListRepo;
import com.kbe.web_shop.service.AuthenticationService;
import com.kbe.web_shop.service.WishListService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WishListControllerIntegrationTest {

    @Autowired
    private WishListController wishListController;


    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private WishListRepo wishListRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private AuthenticationService authenticationService;


    private Product product;

    @Test
    public void testAddToWishList() throws InterruptedException {
        User user = new User();
        user = userRepo.save(user);
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);

        product = newTestProduct();

        ResponseEntity<ApiResponse> apiResponse = wishListController.addToWishList(product,authenticationToken.getToken());
        Thread.sleep(500);

        WishList wishList = wishListRepo.findAllByUserOrderByCreatedDateDesc(user).get(0);

        assertEquals(wishList.getProduct().getId(), product.getId());
        assertEquals(apiResponse.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    public void testDeleteWishListItem() throws InterruptedException {
        User user = new User();
        user = userRepo.save(user);
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);

        product = newTestProduct();

        ResponseEntity<ApiResponse> apiResponse = wishListController.addToWishList(product,authenticationToken.getToken());
        Thread.sleep(500);

        WishList wishList = wishListRepo.findAllByUserOrderByCreatedDateDesc(user).get(0);

        assertEquals(wishList.getProduct().getId(), product.getId());
        assertEquals(apiResponse.getStatusCode(), HttpStatus.CREATED);

        ResponseEntity<ApiResponse> apiResponse2 = wishListController.deleteWishListItem(wishList.getId(), authenticationToken.getToken());

        Thread.sleep(500);

        List <WishList> wishListList = wishListRepo.findAllByUserOrderByCreatedDateDesc(user);
        assertEquals(0, wishListList.size());
        assertEquals(apiResponse2.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testGetWishList() throws InterruptedException {
        User user = new User();
        user = userRepo.save(user);
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);

        product = newTestProduct();

        wishListController.addToWishList(product,authenticationToken.getToken());
        Thread.sleep(500);

        WishList wishListFromRepo = wishListRepo.findAllByUserOrderByCreatedDateDesc(user).get(0);
        WishList wishListFromController = wishListController.getWishList(authenticationToken.getToken()).getBody().get(0);

        assertEquals(wishListFromRepo.getProduct().getId(), product.getId(), wishListFromController.getProduct().getId());
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
}
