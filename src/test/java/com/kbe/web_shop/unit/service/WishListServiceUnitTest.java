package com.kbe.web_shop.unit.service;

import com.kbe.web_shop.exception.CustomException;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.model.WishList;
import com.kbe.web_shop.repository.WishListRepo;
import com.kbe.web_shop.service.ProductService;
import com.kbe.web_shop.service.WishListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WishListServiceUnitTest {

    @InjectMocks
    private WishListService wishListService;

    @Mock
    private WishListRepo wishListRepo;

    @Mock
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateWishlist() {
        WishList wishList = new WishList();

        wishListService.createWishlist(wishList);

        verify(wishListRepo, times(1)).save(wishList);
    }

    @Test
    public void testDeleteWishListItem() {
        Integer itemId = 1;
        User user = new User();

        WishList wishList = new WishList();
        wishList.setUser(user);
        Optional<WishList> optionalWishList = Optional.of(wishList);

        when(wishListRepo.findById(itemId)).thenReturn(optionalWishList);
        doNothing().when(wishListRepo).delete(wishList);

        wishListService.deleteWishListItem(itemId, user);

        verify(wishListRepo, times(1)).delete(wishList);
    }

    @Test
    public void testDeleteWishListItemInvalidId() {
        Integer itemId = 1;
        User user = new User();

        Optional<WishList> optionalWishList = Optional.empty();

        when(wishListRepo.findById(itemId)).thenReturn(optionalWishList);

        assertThrows(CustomException.class, () -> wishListService.deleteWishListItem(itemId, user));
    }

    @Test
    public void testGetWishListForUser() {
        User user = new User();
        List<WishList> wishListItems = new ArrayList<>();
        when(wishListRepo.findAllByUserOrderByCreatedDateDesc(user)).thenReturn(wishListItems);

        List<WishList> result = wishListService.getWishListForUser(user);

        assertNotNull(result);
        assertEquals(wishListItems, result);
    }
}

