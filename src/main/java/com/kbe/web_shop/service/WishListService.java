package com.kbe.web_shop.service;

import com.kbe.web_shop.dto.ProductDto;
import com.kbe.web_shop.exception.CustomException;
import com.kbe.web_shop.model.Cart;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.model.WishList;
import com.kbe.web_shop.repository.WishListRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WishListService {

    @Autowired
    WishListRepo wishListRepo;

    @Autowired
    ProductService productService;

    public void createWishlist(WishList wishList) {
        wishListRepo.save(wishList);
    }

    public void deleteWishListItem(Integer itemId, User user) {


        Optional<WishList> optionalWishList = wishListRepo.findById(itemId);

        if (optionalWishList.isEmpty()) {
            throw new CustomException("wish list item id is invalid: " + itemId);
        }

        WishList wishList = optionalWishList.get();

        if (wishList.getUser() != user) {
            throw  new CustomException("wish list does not belong to user: " +itemId);
        }

        wishListRepo.delete(wishList);
    }

    public List<ProductDto> getWishListForUser(User user) {
        final List<WishList> wishLists = wishListRepo.findAllByUserOrderByCreatedDateDesc(user);
        List<ProductDto> productDtos = new ArrayList<>();
        for (WishList wishList: wishLists) {
            productDtos.add(productService.getProductDto(wishList.getProduct()));
        }

        return productDtos;
    }

}
