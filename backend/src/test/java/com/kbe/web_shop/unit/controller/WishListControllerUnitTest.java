package com.kbe.web_shop.unit.controller;

import com.kbe.web_shop.controller.WishListController;
import com.kbe.web_shop.config.common.ApiResponse;
import com.kbe.web_shop.model.Product;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.model.WishList;
import com.kbe.web_shop.producer.WishListProducer;
import com.kbe.web_shop.service.AuthenticationService;
import com.kbe.web_shop.service.WishListService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class WishListControllerUnitTest {

    @Mock
    private WishListService wishListService;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private WishListProducer wishListProducer;

    @InjectMocks
    private WishListController wishListController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddToWishListSuccess() {
        String token = "validToken";
        Product product = new Product();
        User user = new User();
        ApiResponse expectedApiResponse = new ApiResponse(true, "Added to wishlist");

        when(authenticationService.getUser(token)).thenReturn(user);

        ResponseEntity<ApiResponse> responseEntity = wishListController.addToWishList(product, token);
        ApiResponse apiResponse = responseEntity.getBody();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedApiResponse.getMessage(), apiResponse.getMessage());
    }

    @Test
    public void testDeleteWishListItemSuccess() {
        String token = "validToken";
        Integer itemId = 1;
        User user = new User();
        ApiResponse expectedApiResponse = new ApiResponse(true, "Item has been removed");

        //when(authenticationService.authenticate(token)).thenReturn(null);
        when(authenticationService.getUser(token)).thenReturn(user);
        //when(wishListService.deleteWishListItem(itemId, user)).thenReturn(null);

        ResponseEntity<ApiResponse> responseEntity = wishListController.deleteWishListItem(itemId, token);
        ApiResponse apiResponse = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedApiResponse.getMessage(), apiResponse.getMessage());
    }

    @Test
    public void testGetWishListSuccess() {
        String token = "validToken";
        User user = new User();
        List<WishList> wishList = new ArrayList<>();
        WishList wishListItem1 = new WishList(user, new Product());
        WishList wishListItem2 = new WishList(user, new Product());
        wishList.add(wishListItem1);
        wishList.add(wishListItem2);

        when(authenticationService.getUser(token)).thenReturn(user);
        when(wishListService.getWishListForUser(user)).thenReturn(wishList);

        ResponseEntity<List<WishList>> responseEntity = wishListController.getWishList(token);
        List<WishList> responseWishList = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(wishList, responseWishList);
    }
}

