package com.kbe.web_shop.integration.controller;

import com.kbe.web_shop.config.common.ApiResponse;
import com.kbe.web_shop.config.constants.Role;
import com.kbe.web_shop.controller.CategoryController;
import com.kbe.web_shop.model.AuthenticationToken;
import com.kbe.web_shop.model.Category;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.repository.CategoryRepo;
import com.kbe.web_shop.repository.UserRepo;
import com.kbe.web_shop.service.AuthenticationService;
import com.kbe.web_shop.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryControllerIntegrationTest {

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

    private Category category;

    @BeforeEach
    public void setUp(){
        category = new Category();
    }

    @Test
    public void testCreateCategoryWithEditPermission() throws InterruptedException {
        User user = new User();
        user.setRole(Role.storehouse);
        user = userRepo.save(user);
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);

        int prevSize = categoryRepo.findAll().size();
        ResponseEntity<ApiResponse> apiResponse = categoryController.createCategory(authenticationToken.getToken(), category);
        Thread.sleep(500);
        int newSize = categoryRepo.findAll().size();
        assertEquals(prevSize, newSize-1);
        assertEquals(apiResponse.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    public void testCreateCategoryWithoutEditPermission() throws InterruptedException {
        User user = new User();
        user.setRole(Role.customer);
        user = userRepo.save(user);
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);

        int prevSize = categoryRepo.findAll().size();
        ResponseEntity<ApiResponse> apiResponse = categoryController.createCategory(authenticationToken.getToken(), category);
        Thread.sleep(500);
        int newSize = categoryRepo.findAll().size();
        assertEquals(prevSize, newSize);
        assertEquals(apiResponse.getStatusCode(), HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testEditCategoryWithEditPermission() throws InterruptedException {
        User user = new User();
        user.setRole(Role.storehouse);
        user = userRepo.save(user);
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);

        category.setCategoryName("Added");
        categoryService.createCategory(category);
        Category editCategory = categoryRepo.findFirstByOrderByIdDesc();
        assertEquals(editCategory.getCategoryName(), "Added");
        editCategory.setCategoryName("Edited");
        ResponseEntity<ApiResponse> apiResponse = categoryController.updateCategory(authenticationToken.getToken(), editCategory.getId(), editCategory);
        Thread.sleep(500);
        assertEquals(categoryRepo.findFirstByOrderByIdDesc().getCategoryName(), "Edited");
        assertEquals(apiResponse.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testEditCategoryWithoutEditPermission() throws InterruptedException {
        User user = new User();
        user.setRole(Role.customer);
        user = userRepo.save(user);
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);

        category.setCategoryName("Added");
        categoryService.createCategory(category);
        Category editCategory = categoryRepo.findFirstByOrderByIdDesc();
        assertEquals(editCategory.getCategoryName(), "Added");
        editCategory.setCategoryName("Edited");
        ResponseEntity<ApiResponse> apiResponse = categoryController.updateCategory(authenticationToken.getToken(), editCategory.getId(), editCategory);
        Thread.sleep(500);
        assertEquals(categoryRepo.findFirstByOrderByIdDesc().getCategoryName(), "Added");
        assertEquals(apiResponse.getStatusCode(), HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testEditCategoryWithEditPermissionInvalidCategory() throws InterruptedException {
        User user = new User();
        user.setRole(Role.storehouse);
        user = userRepo.save(user);
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);

        Category invalidCategory = new Category();
        invalidCategory.setId(Integer.MAX_VALUE);

        ResponseEntity<ApiResponse> apiResponse = categoryController.updateCategory(authenticationToken.getToken(), invalidCategory.getId(), invalidCategory);
        Thread.sleep(500);
        assertEquals(apiResponse.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDeleteCategoryWithEditPermission() throws InterruptedException {
        User user = new User();
        user.setRole(Role.storehouse);
        user = userRepo.save(user);
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);

        categoryService.createCategory(category);
        int categoryId = categoryRepo.findFirstByOrderByIdDesc().getId();
        int prevSize = categoryRepo.findAll().size();
        ResponseEntity<ApiResponse> apiResponse = categoryController.deleteCategory(authenticationToken.getToken(),categoryId);
        Thread.sleep(500);
        int newSize = categoryRepo.findAll().size();
        assertEquals(prevSize, newSize+1);
        assertEquals(apiResponse.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testDeleteCategoryWithoutEditPermission() throws InterruptedException {
        User user = new User();
        user.setRole(Role.customer);
        user = userRepo.save(user);
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);

        categoryService.createCategory(category);
        int categoryId = categoryRepo.findFirstByOrderByIdDesc().getId();
        int prevSize = categoryRepo.findAll().size();
        ResponseEntity<ApiResponse> apiResponse = categoryController.deleteCategory(authenticationToken.getToken(),categoryId);
        Thread.sleep(500);
        int newSize = categoryRepo.findAll().size();
        assertEquals(prevSize, newSize);
        assertEquals(apiResponse.getStatusCode(), HttpStatus.UNAUTHORIZED);
    }
}
