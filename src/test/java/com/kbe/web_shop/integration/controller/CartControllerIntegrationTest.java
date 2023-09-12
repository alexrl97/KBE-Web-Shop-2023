package com.kbe.web_shop.integration.controller;

import com.kbe.web_shop.controller.CartController;
import com.kbe.web_shop.dto.cart.CartDto;
import com.kbe.web_shop.model.AuthenticationToken;
import com.kbe.web_shop.model.Category;
import com.kbe.web_shop.model.Product;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.repository.CartRepo;
import com.kbe.web_shop.repository.CategoryRepo;
import com.kbe.web_shop.repository.ProductRepo;
import com.kbe.web_shop.repository.UserRepo;
import com.kbe.web_shop.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartControllerIntegrationTest {


    @Autowired
    private CartController cartController;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;


    @Test
    public void testAddToCart() throws InterruptedException {
        User user = new User();
        user = userRepo.save(user);
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);

        CartDto cartDto = new CartDto();
        cartDto.setId(1000);
        cartDto.setQuantity(1);
        cartDto.setProductId(getTestProduct().getId());

        int prevSize = cartRepo.findAll().size();
        cartController.addToCart(cartDto, authenticationToken.getToken());

        Thread.sleep(500);

        int currentSize = cartRepo.findAll().size();
        assertEquals(prevSize, currentSize-1);

    }

    @Test
    public void testDeleteCartItem() throws InterruptedException {
        User user = new User();
        user = userRepo.save(user);
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);

        CartDto cartDto = new CartDto();
        cartDto.setId(1000);
        cartDto.setQuantity(1);
        cartDto.setProductId(getTestProduct().getId());

        cartController.addToCart(cartDto, authenticationToken.getToken());

        Thread.sleep(500);

        assertEquals(cartRepo.findAllByUserOrderByCreatedDateDesc(user).size(), 1);

        int cartId = cartRepo.findAllByUserOrderByCreatedDateDesc(user).get(0).getId();

        cartController.deleteCartItem(cartId, authenticationToken.getToken());
        Thread.sleep(500);

        assertEquals(cartRepo.findAllByUserOrderByCreatedDateDesc(user).size(), 0);
    }

    @Test
    public void testGetCartItems() throws InterruptedException {
        User user = new User();
        user = userRepo.save(user);
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);

        CartDto cartDto = new CartDto();
        cartDto.setId(1000);
        cartDto.setQuantity(1);
        cartDto.setProductId(getTestProduct().getId());

        cartController.addToCart(cartDto, authenticationToken.getToken());

        Thread.sleep(500);

        assertEquals(cartController.getCartItems(authenticationToken.getToken()).getBody().getCartItems().size(),1);
    }



    private Product getTestProduct(){
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
