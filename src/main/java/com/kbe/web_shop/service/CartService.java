package com.kbe.web_shop.service;

import com.kbe.web_shop.dto.cart.CartDto;
import com.kbe.web_shop.dto.cart.CartListDto;
import com.kbe.web_shop.dto.cart.CartItemDto;
import com.kbe.web_shop.exception.CustomException;
import com.kbe.web_shop.model.Cart;
import com.kbe.web_shop.model.Product;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.repository.CartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {

    @Autowired
    ProductService productService;

    @Autowired
    CartRepo cartRepository;

    public void addToCart(CartDto cartDto) {

        // validate if the product id is valid
        Product product = productService.findById(cartDto.getProductId());

        Cart cart = new Cart();
        cart.setProduct(product);
        cart.setUser(cartDto.getUser());
        cart.setQuantity(cartDto.getQuantity());
        cart.setCreatedDate(new Date());

        // save the cart
        cartRepository.save(cart);
    }

    public CartListDto listCartItems(User user) {
        List<Cart> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);

        List<CartItemDto> cartItems = new ArrayList<>();
        double totalCost = 0;
        for (Cart cart: cartList) {
            CartItemDto cartItemDto = new CartItemDto(cart);
            totalCost += cartItemDto.getQuantity() * cart.getProduct().getPrice();
            cartItems.add(cartItemDto);
        }

        CartListDto cartListDto = new CartListDto();
        cartListDto.setTotalCost(totalCost);
        cartListDto.setCartItems(cartItems);
        return cartListDto;
    }


    public void updateCartItem(CartDto cartDto){
        Cart cart = cartRepository.getOne(cartDto.getId());
        cart.setQuantity(cartDto.getQuantity());
        cart.setCreatedDate(new Date());
        cartRepository.save(cart);
    }

    public void deleteCartItem(Integer cartItemId, User user) {
        // the item id belongs to user

        Optional<Cart> optionalCart = cartRepository.findById(cartItemId);

        if (optionalCart.isEmpty()) {
            throw new CustomException("cart item id is invalid: " + cartItemId);
        }

        Cart cart = optionalCart.get();

        if (!Objects.equals(cart.getUser().getEmail(), user.getEmail())) {
            throw  new CustomException("cart item does not belong to user: " +cartItemId);
        }

        cartRepository.delete(cart);
    }

    public void deleteUserCartItems(User user) {

        List<Cart> userCartItems = cartRepository.findAllByUserOrderByCreatedDateDesc(user);
        cartRepository.deleteAll(userCartItems);
    }
}
