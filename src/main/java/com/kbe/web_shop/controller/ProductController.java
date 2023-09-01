package com.kbe.web_shop.controller;

import com.kbe.web_shop.common.ApiResponse;
import com.kbe.web_shop.dto.product.ProductDto;
import com.kbe.web_shop.model.Category;
import com.kbe.web_shop.producer.ProductProducer;
import com.kbe.web_shop.repository.CategoryRepo;
import com.kbe.web_shop.service.AuthenticationService;
import com.kbe.web_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    ProductProducer productProducer;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createProduct(@RequestParam("token") String token, @RequestBody ProductDto productDto) {
        if(authenticationService.hasEditPermission(token)) {
            Optional<Category> optionalCategory = categoryRepo.findById(productDto.getCategoryId());
            if (!optionalCategory.isPresent()) {
                return new ResponseEntity<ApiResponse>(new ApiResponse(false, "category does not exists"), HttpStatus.BAD_REQUEST);
            }
            productProducer.sendCreateMessage(productDto);
            //productService.createProduct(productDto, optionalCategory.get());
            return new ResponseEntity<ApiResponse>(new ApiResponse(true, "product has been added"), HttpStatus.CREATED);
        }
        return new ResponseEntity<ApiResponse>(new ApiResponse(false, "only storehouse users can create a new product"), HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductDto>> getProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // create an api to edit the product


    @PostMapping("/update/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@RequestParam("token") String token, @PathVariable("productId") Integer productId, @RequestBody ProductDto productDto) throws Exception {
        if(authenticationService.hasEditPermission(token)) {
            Optional<Category> optionalCategory = categoryRepo.findById(productDto.getCategoryId());
            if (!optionalCategory.isPresent()) {
                return new ResponseEntity<ApiResponse>(new ApiResponse(false, "category does not exists"), HttpStatus.BAD_REQUEST);
            }
            productProducer.sendUpdateMessage(productDto);
            //productService.updateProduct(productDto, productId);
            return new ResponseEntity<ApiResponse>(new ApiResponse(true, "product has been updated"), HttpStatus.OK);
        }
        return new ResponseEntity<ApiResponse>(new ApiResponse(false, "only storehouse users can update a product"), HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping ("/delete/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@RequestParam("token") String token, @PathVariable("productId") Integer productId) throws Exception {
        if(authenticationService.hasEditPermission(token)) {
            productProducer.sendDeleteMessage(productId.toString());
            //productService.deleteProduct(productId);
            return new ResponseEntity<ApiResponse>(new ApiResponse(true, "product has been deleted"), HttpStatus.OK);
        }
        return new ResponseEntity<ApiResponse>(new ApiResponse(false, "only storehouse users can delete a product"), HttpStatus.UNAUTHORIZED);
    }

}
