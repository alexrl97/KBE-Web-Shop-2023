package com.kbe.web_shop.integration.controller;

import com.kbe.web_shop.common.ApiResponse;
import com.kbe.web_shop.config.constants.Role;
import com.kbe.web_shop.controller.CategoryController;
import com.kbe.web_shop.controller.ProductController;
import com.kbe.web_shop.dto.product.ProductDto;
import com.kbe.web_shop.model.AuthenticationToken;
import com.kbe.web_shop.model.Category;
import com.kbe.web_shop.model.Product;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.repository.CategoryRepo;
import com.kbe.web_shop.repository.ProductRepo;
import com.kbe.web_shop.repository.UserRepo;
import com.kbe.web_shop.service.AuthenticationService;
import com.kbe.web_shop.service.CategoryService;
import com.kbe.web_shop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductControllerIntegrationTest {

    @Autowired
    private ProductController productController;

    @Autowired
    private CategoryController categoryController;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductService productService;


    @Test
    public void testCreateProductWithEditPermission() throws InterruptedException {
        User user = new User();
        user.setRole(Role.storehouse);
        user = userRepo.save(user);
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);

        ProductDto productDto = new ProductDto();
        String productName = getRandomString();
        productDto.setName(productName);
        Category category = new Category();
        category.setCategoryName("Test Category");
        categoryRepo.save(category);
        category = categoryRepo.findFirstByOrderByIdDesc();
        productDto.setCategoryId(category.getId());

        ResponseEntity<ApiResponse> apiResponse = productController.createProduct(authenticationToken.getToken(), productDto);

        assertEquals(apiResponse.getStatusCode(), HttpStatus.CREATED);

        Thread.sleep(500);

        productDto = productService.getAllProducts().stream().filter(X -> Objects.equals(X.getName(), productName)).toList().get(0);

        assertNotNull(productDto);
    }

    @Test
    public void testCreateProductWithoutEditPermission() throws InterruptedException {
        User user = new User();
        user.setRole(Role.customer);
        user = userRepo.save(user);
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);

        ProductDto productDto = new ProductDto();
        String productName = getRandomString();
        productDto.setName(productName);
        Category category = new Category();
        category.setCategoryName("Test Category");
        categoryRepo.save(category);
        category = categoryRepo.findFirstByOrderByIdDesc();
        productDto.setCategoryId(category.getId());

        ResponseEntity<ApiResponse> apiResponse = productController.createProduct(authenticationToken.getToken(), productDto);

        assertEquals(apiResponse.getStatusCode(), HttpStatus.UNAUTHORIZED);

        List<ProductDto>productDtoList = productService.getAllProducts().stream().filter(X -> Objects.equals(X.getName(), productName)).toList();

        assertEquals(productDtoList.size(), 0);
    }

    @Test
    public void testCreateProductWithEditPermissionInvalidCategory(){
        User user = new User();
        user.setRole(Role.storehouse);
        user = userRepo.save(user);
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);

        ProductDto productDto = new ProductDto();
        productDto.setName("Test Product");
        productDto.setDescription("This is a test product.");
        productDto.setImageURL("test-image.jpg");
        productDto.setPrice(19.99);
        productDto.setDeckCardId("12345");
        productDto.setRarity("Common");
        productDto.setCategoryId(Integer.MAX_VALUE);


        ResponseEntity<ApiResponse> apiResponse = productController.createProduct(authenticationToken.getToken(), productDto);

        assertEquals(apiResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testGetProducts(){

        ProductDto productDto = new ProductDto();
        productDto.setName("Test Product");
        productDto.setDescription("This is a test product.");
        productDto.setImageURL("test-image.jpg");
        productDto.setPrice(19.99);
        productDto.setDeckCardId("12345");
        productDto.setRarity("Common");
        Category category = new Category();
        category.setCategoryName("Test Category");
        categoryRepo.save(category);

        productService.createProduct(productDto, category);

        ResponseEntity<List<ProductDto>> apiResponse = productController.getProducts();

        assertEquals(apiResponse.getStatusCode(), HttpStatus.OK);
        assertTrue(apiResponse.getBody().size() >= 1);
    }

    @Test
    public void testEditProductWithEditPermission() throws InterruptedException {
        User user = new User();
        user.setRole(Role.storehouse);
        user = userRepo.save(user);
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);

        ProductDto productDto = getTestProductDto();
        String oldName = productDto.getName();
        String newName = getRandomString();
        productDto.setName(newName);

        ResponseEntity<ApiResponse> apiResponse = productController.updateProduct(authenticationToken.getToken(),productDto);
        Thread.sleep(500);

        List<Product> allProducts = productRepo.findAll();

        Product updatedProduct = allProducts.stream()
                .filter(p -> p.getName().equals(newName))
                .findFirst()
                .orElse(null);

        assertEquals(apiResponse.getStatusCode(), HttpStatus.OK);
        assertEquals(updatedProduct.getId(), productDto.getId());
        assertNotEquals(oldName, updatedProduct.getName());
    }

    @Test
    public void testEditProductWithoutEditPermission() throws InterruptedException {
        User user = new User();
        user.setRole(Role.customer);
        user = userRepo.save(user);
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);

        ProductDto productDto = getTestProductDto();
        String oldName = productDto.getName();
        String newName = getRandomString();
        productDto.setName(newName);

        ResponseEntity<ApiResponse> apiResponse = productController.updateProduct(authenticationToken.getToken(),productDto);
        Thread.sleep(500);

        List<Product> allProducts = productRepo.findAll();

        Product updatedProduct = allProducts.stream()
                .filter(p -> p.getId().equals(productDto.getId()))
                .findFirst()
                .orElse(null);

        assertEquals(apiResponse.getStatusCode(), HttpStatus.UNAUTHORIZED);
        assertEquals(oldName, updatedProduct.getName());

    }

    @Test
    public void testEditProductWithEditPermissionInvalidCategory() throws InterruptedException {
        User user = new User();
        user.setRole(Role.storehouse);
        user = userRepo.save(user);
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);

        ProductDto productDto = getTestProductDto();
        productDto.setCategoryId(Integer.MAX_VALUE);

        ResponseEntity<ApiResponse> apiResponse = productController.updateProduct(authenticationToken.getToken(),productDto);
        Thread.sleep(500);
        assertEquals(apiResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testDeleteProductWithEditPermission() throws InterruptedException {
        User user = new User();
        user.setRole(Role.storehouse);
        user = userRepo.save(user);
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);

        ProductDto productDto = getTestProductDto();

        assertNotNull(productDto);

        ResponseEntity<ApiResponse> apiResponse = productController.deleteProduct(authenticationToken.getToken(),productDto.getId());
        Thread.sleep(500);


        List<Product> allProducts = productRepo.findAll();

        Product deletedProduct = allProducts.stream()
                .filter(p -> p.getId().equals(productDto.getId()))
                .findFirst()
                .orElse(null);

        assertNull(deletedProduct);
        assertEquals(apiResponse.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testDeleteProductWithoutEditPermission() throws InterruptedException {
        User user = new User();
        user.setRole(Role.customer);
        user = userRepo.save(user);
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);

        ProductDto productDto = getTestProductDto();

        assertNotNull(productDto);

        ResponseEntity<ApiResponse> apiResponse = productController.deleteProduct(authenticationToken.getToken(),productDto.getId());
        Thread.sleep(500);


        List<Product> allProducts = productRepo.findAll();

        Product deletedProduct = allProducts.stream()
                .filter(p -> p.getId().equals(productDto.getId()))
                .findFirst()
                .orElse(null);

        assertNotNull(deletedProduct);
        assertEquals(apiResponse.getStatusCode(), HttpStatus.UNAUTHORIZED);
    }

    private ProductDto getTestProductDto(){
        Category category = new Category();
        category.setCategoryName("Test Category");
        category.setId(100);
        category = categoryRepo.findFirstByOrderByIdDesc();

        Product product1 = new Product();
        String name = getRandomString();
        product1.setName(name);
        product1.setDescription("Description 1");
        product1.setImageURL("image1.jpg");
        product1.setPrice(19.99);
        product1.setDeckCardId("12345");
        product1.setRarity("Common");
        product1.setCategory(category);
        productRepo.save(product1);

        List<Product> allProducts = productRepo.findAll();

        Product product = allProducts.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElse(null);

        return new ProductDto(product);
    }

    private String getRandomString(){
        Random random = new Random();
        String generatedString = random.ints(92, 123 + 1)
                .limit(10)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }
}
