package com.kbe.web_shop.service;

import com.kbe.web_shop.dto.product.ProductDto;
import com.kbe.web_shop.exception.CustomException;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.model.WishList;
import com.kbe.web_shop.repository.WishListRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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

        if (!Objects.equals(wishList.getUser().getEmail(), user.getEmail())) {
            throw  new CustomException("wish list does not belong to user: " +user.getEmail() + " " + wishList.getUser().getEmail());
        }

        wishListRepo.delete(wishList);
    }

    public List<WishList> getWishListForUser(User user) {
        return wishListRepo.findAllByUserOrderByCreatedDateDesc(user);
    }
}
