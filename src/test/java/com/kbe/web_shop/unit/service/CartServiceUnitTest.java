package com.kbe.web_shop.unit.service;

import com.kbe.web_shop.dto.cart.CartDto;
import com.kbe.web_shop.dto.cart.CartListDto;
import com.kbe.web_shop.exception.CustomException;
import com.kbe.web_shop.model.Cart;
import com.kbe.web_shop.model.Product;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.repository.CartRepo;
import com.kbe.web_shop.service.CartService;
import com.kbe.web_shop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartServiceUnitTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private ProductService productService;

    @Mock
    private CartRepo cartRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddToCart() {
        CartDto cartDto = new CartDto();
        cartDto.setProductId(1);
        cartDto.setUser(new User());
        cartDto.setQuantity(2);

        Product product = new Product();
        product.setPrice(10.0);

        when(productService.findById(cartDto.getProductId())).thenReturn(product);

        cartService.addToCart(cartDto);

        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    public void testListCartItems() {
        User user = new User();
        Cart cart1 = new Cart();
        cart1.setQuantity(2);
        Product product1 = new Product();
        product1.setPrice(10.0);
        cart1.setProduct(product1);

        Cart cart2 = new Cart();
        cart2.setQuantity(3);
        Product product2 = new Product();
        product2.setPrice(15.0);
        cart2.setProduct(product2);

        List<Cart> cartList = Arrays.asList(cart1, cart2);

        when(cartRepository.findAllByUserOrderByCreatedDateDesc(user)).thenReturn(cartList);

        CartListDto cartListDto = cartService.listCartItems(user);

        assertNotNull(cartListDto);
        assertEquals(2, cartListDto.getCartItems().size());
        assertEquals(65.0, cartListDto.getTotalCost(), 0.001);
    }

    @Test
    public void testDeleteCartItem() {
        Integer cartItemId = 1;
        User user = new User();

        Cart cart = new Cart();
        cart.setId(cartItemId);
        cart.setUser(user);

        when(cartRepository.findById(cartItemId)).thenReturn(Optional.of(cart));

        assertDoesNotThrow(() -> cartService.deleteCartItem(cartItemId, user));
    }

    @Test
    public void testDeleteCartItemWithInvalidItemId() {
        Integer cartItemId = 1;
        User user = new User();

        when(cartRepository.findById(cartItemId)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> cartService.deleteCartItem(cartItemId, user));
    }


    @Test
    public void testDeleteUserCartItems() {
        User user = new User();
        Cart cart1 = new Cart();
        Cart cart2 = new Cart();
        List<Cart> userCartItems = Arrays.asList(cart1, cart2);

        when(cartRepository.findAllByUserOrderByCreatedDateDesc(user)).thenReturn(userCartItems);

        cartService.deleteUserCartItems(user);

        verify(cartRepository, times(1)).deleteAll(userCartItems);
    }
}

