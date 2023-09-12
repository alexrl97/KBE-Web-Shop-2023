package com.kbe.web_shop.controller;

import com.kbe.web_shop.common.ApiResponse;
import com.kbe.web_shop.dto.product.ProductDto;
import com.kbe.web_shop.dto.wishList.WishListDeleteDto;
import com.kbe.web_shop.model.Product;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.model.WishList;
import com.kbe.web_shop.producer.WishListProducer;
import com.kbe.web_shop.service.AuthenticationService;
import com.kbe.web_shop.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/wishlist")
public class WishListController {

    @Autowired
    WishListService wishListService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    WishListProducer wishListProducer;


    // save product as wishlist item
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToWishList(@RequestBody Product product,
                                                     @RequestParam("token") String token) {
        // authenticate the token
        authenticationService.authenticate(token);

        // find the user

        User user = authenticationService.getUser(token);

        // save the item in wishlist

        WishList wishList = new WishList(user, product);

        //wishListService.createWishlist(wishList);
        wishListProducer.sendCreateMessage(wishList);

        ApiResponse apiResponse = new ApiResponse(true, "Added to wishlist");
        return  new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{wishListItemId}")
    public ResponseEntity<ApiResponse> deleteWishListItem(@PathVariable("wishListItemId") Integer itemId,
                                                          @RequestParam("token") String token) {

        // authenticate the token
        authenticationService.authenticate(token);

        // find the user
        User user = authenticationService.getUser(token);

        //wishListService.deleteWishListItem(itemId, user);
        wishListProducer.sendDeleteMessage(new WishListDeleteDto(itemId, user));

        return new ResponseEntity<>(new ApiResponse(true, "Item has been removed"), HttpStatus.OK);
    }


    // get all wishlist item for a user

    @GetMapping("/{token}")
    public ResponseEntity<List<WishList>> getWishList(@PathVariable("token") String token) {

        // authenticate the token
        authenticationService.authenticate(token);


        // find the user

        User user = authenticationService.getUser(token);

        List<WishList> productDtos = wishListService.getWishListForUser(user);

        return new ResponseEntity<>(productDtos, HttpStatus.OK);

    }



}
