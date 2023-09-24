package com.kbe.web_shop.controller;

import com.kbe.web_shop.model.Category;
import com.kbe.web_shop.producer.CategoryProducer;
import com.kbe.web_shop.service.AuthenticationService;
import com.kbe.web_shop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kbe.web_shop.config.common.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    CategoryProducer categoryProducer;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCategory(@RequestParam("token") String token, @RequestBody Category category){

        if(authenticationService.hasEditPermission(token)) {
            //categoryService.createCategory(category);
            categoryProducer.sendCreateMessage(category);
            return new ResponseEntity<ApiResponse>(new ApiResponse(true, "a new category created"), HttpStatus.CREATED);
        }
        return new ResponseEntity<ApiResponse>(new ApiResponse(false, "only storehouse users can create a new category"), HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/list")
    public List<Category> listCategory() {
        return categoryService.listCategory();
    }

    @PostMapping("/update/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategory(@RequestParam("token") String token, @PathVariable("categoryId") int categoryId, @RequestBody Category category){

        if(authenticationService.hasEditPermission(token)) {
            if (!categoryService.findById(categoryId)) {
                return new ResponseEntity<ApiResponse>(new ApiResponse(false, "category does not exist"), HttpStatus.NOT_FOUND);
            }
            //categoryService.editCategory(categoryId, category);
            categoryProducer.sendUpdateMessage(category);
            return new ResponseEntity<ApiResponse>(new ApiResponse(true, "category has been updated"), HttpStatus.OK);
        }
        return new ResponseEntity<ApiResponse>(new ApiResponse(false, "only storehouse users can update a category"), HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping ("/delete/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@RequestParam("token") String token, @PathVariable("categoryId") Integer categoryId) {
        if(authenticationService.hasEditPermission(token)) {
            //categoryService.deleteCategory(categoryId);
            categoryProducer.sendDeleteMessage(categoryId.toString());
            return new ResponseEntity<ApiResponse>(new ApiResponse(true, "category has been deleted"), HttpStatus.OK);
        }
        return new ResponseEntity<ApiResponse>(new ApiResponse(false, "only storehouse users can delete a category"), HttpStatus.UNAUTHORIZED);
    }
}
