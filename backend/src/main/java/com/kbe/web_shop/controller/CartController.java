package com.kbe.web_shop.controller;

import com.kbe.web_shop.common.ApiResponse;
import com.kbe.web_shop.dto.cart.CartDeleteItemDto;
import com.kbe.web_shop.dto.cart.CartDto;
import com.kbe.web_shop.dto.cart.CartListDto;
import com.kbe.web_shop.exception.AuthenticationFailException;
import com.kbe.web_shop.exception.ProductNotExistsException;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.producer.CartProducer;
import com.kbe.web_shop.service.AuthenticationService;
import com.kbe.web_shop.service.CartService;
import com.kbe.web_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;


    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private CartProducer cartProducer;


    // post cart api
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToCart(@RequestBody CartDto cartDto,
                                                 @RequestParam("token") String token) {
        // authenticate the token
        authenticationService.authenticate(token);


        // find the user

        User user = authenticationService.getUser(token);
        cartDto.setUser(user);


        //cartService.addToCart(addToCartDto);
        cartProducer.sendAddToCartMessage(cartDto);

        return new ResponseEntity<>(new ApiResponse(true, "Added to cart"), HttpStatus.CREATED);
    }


    // get all cart items for a user
    @GetMapping("/")
    public ResponseEntity<CartListDto> getCartItems(@RequestParam("token") String token) {
        // authenticate the token
        authenticationService.authenticate(token);

        // find the user
        User user = authenticationService.getUser(token);

        // get cart items

        CartListDto cartListDto = cartService.listCartItems(user);
        return new ResponseEntity<>(cartListDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable("cartItemId") Integer itemId,
                                                      @RequestParam("token") String token) {

        // authenticate the token
        authenticationService.authenticate(token);

        // find the user
        User user = authenticationService.getUser(token);

        //cartService.deleteCartItem(itemId, user);
        cartProducer.sendDeleteCartItemMessage(new CartDeleteItemDto(itemId,user));

        return new ResponseEntity<>(new ApiResponse(true, "Item has been removed"), HttpStatus.OK);
    }

}
