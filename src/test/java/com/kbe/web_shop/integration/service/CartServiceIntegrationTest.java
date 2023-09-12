package com.kbe.web_shop.integration.service;

import static org.junit.jupiter.api.Assertions.*;

import com.kbe.web_shop.dto.cart.CartDto;
import com.kbe.web_shop.exception.CustomException;
import com.kbe.web_shop.model.Category;
import com.kbe.web_shop.model.Product;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.repository.CartRepo;
import com.kbe.web_shop.repository.CategoryRepo;
import com.kbe.web_shop.repository.ProductRepo;
import com.kbe.web_shop.repository.UserRepo;
import com.kbe.web_shop.service.CartService;
import com.kbe.web_shop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartServiceIntegrationTest  {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private UserRepo userRepo;

    private User user;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @BeforeEach
    public void setUp(){
        user = new User();
        userRepo.save(user);
    }

    @Test
    public void testAddToCart(){
        int prevSize = cartRepo.findAll().size();
        CartDto cartDto = new CartDto();
        cartDto.setId(1000);
        cartDto.setQuantity(1);
        cartDto.setProductId(getTestProduct().getId());
        cartService.addToCart(cartDto);
        int currentSize = cartRepo.findAll().size();
        assertEquals(prevSize, currentSize-1);
    }

    @Test
    public void testListCartItems(){

        CartDto cartDto1 = new CartDto();
        cartDto1.setId(1000);
        cartDto1.setQuantity(1);
        cartDto1.setUser(user);
        cartDto1.setProductId(getTestProduct().getId());
        CartDto cartDto2 = new CartDto();
        cartDto2.setId(1001);
        cartDto2.setQuantity(1);
        cartDto2.setUser(user);
        cartDto2.setProductId(getTestProduct().getId());

        cartService.addToCart(cartDto1);
        cartService.addToCart(cartDto2);

        assertEquals(cartService.listCartItems(user).getCartItems().size(), 2);
    }

    @Test
    public void testDeleteCartItem(){

        CartDto cartDto1 = new CartDto();
        cartDto1.setId(1001);
        cartDto1.setQuantity(1);
        cartDto1.setUser(user);
        cartDto1.setProductId(getTestProduct().getId());
        cartService.addToCart(cartDto1);
        int cartId = cartRepo.findAllByUserOrderByCreatedDateDesc(user).get(0).getId();
        assertEquals(cartRepo.findAllByUserOrderByCreatedDateDesc(user).size(), 1);
        cartService.deleteCartItem(cartId, user);
        assertEquals(cartRepo.findAllByUserOrderByCreatedDateDesc(user).size(), 0);
    }

    @Test
    public void testDeleteCartItemThrowsExceptionWhenCartNotFound() {
        assertThrows(CustomException.class, () -> {
            cartService.deleteCartItem(Integer.MAX_VALUE, new User());
        });
    }

    @Test
    public void testDeleteCartItemThrowsExceptionWhenCartDoesNotBelongToUser() {
        User falseUser = new User();
        userRepo.save(falseUser);

        CartDto cartDto1 = new CartDto();
        cartDto1.setId(1001);
        cartDto1.setQuantity(1);
        cartDto1.setUser(user);
        cartDto1.setProductId(getTestProduct().getId());
        cartService.addToCart(cartDto1);
        int cartId = cartRepo.findAllByUserOrderByCreatedDateDesc(user).get(0).getId();
        assertEquals(cartRepo.findAllByUserOrderByCreatedDateDesc(user).size(), 1);
        cartService.deleteCartItem(cartId, user);

        assertThrows(CustomException.class, () -> {
            cartService.deleteCartItem(cartId, falseUser);
        });
    }

    @Test
    public void testDeleteUserCartItems(){

        CartDto cartDto1 = new CartDto();
        cartDto1.setId(1001);
        cartDto1.setQuantity(1);
        cartDto1.setUser(user);
        cartDto1.setProductId(getTestProduct().getId());
        cartService.addToCart(cartDto1);
        cartService.addToCart(cartDto1);
        assertEquals(cartRepo.findAllByUserOrderByCreatedDateDesc(user).size(), 2);
        cartService.deleteUserCartItems(user);
        assertEquals(cartRepo.findAllByUserOrderByCreatedDateDesc(user).size(), 0);
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












