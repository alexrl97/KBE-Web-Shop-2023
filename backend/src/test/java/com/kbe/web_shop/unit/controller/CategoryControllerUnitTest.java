package com.kbe.web_shop.unit.controller;
import com.kbe.web_shop.common.ApiResponse;
import com.kbe.web_shop.controller.CategoryController;
import com.kbe.web_shop.model.Category;
import com.kbe.web_shop.producer.CategoryProducer;
import com.kbe.web_shop.service.AuthenticationService;
import com.kbe.web_shop.service.CategoryService;
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

public class CategoryControllerUnitTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private CategoryProducer categoryProducer;

    @InjectMocks
    private CategoryController categoryController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateCategorySuccess() {
        String token = "validToken";
        Category category = new Category();

        when(authenticationService.hasEditPermission(token)).thenReturn(true);

        ResponseEntity<ApiResponse> response = categoryController.createCategory(token, category);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("a new category created", response.getBody().getMessage());

        verify(categoryProducer, times(1)).sendCreateMessage(category);
    }

    @Test
    public void testCreateCategoryUnauthorized() {
        String token = "invalidToken";
        Category category = new Category();

        when(authenticationService.hasEditPermission(token)).thenReturn(false);

        ResponseEntity<ApiResponse> response = categoryController.createCategory(token, category);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("only storehouse users can create a new category", response.getBody().getMessage());

        verify(categoryProducer, never()).sendCreateMessage(category);
    }

    @Test
    public void testListCategory() {
        List<Category> categories = new ArrayList<>();
        when(categoryService.listCategory()).thenReturn(categories);

        List<Category> result = categoryController.listCategory();

        assertEquals(categories, result);
    }

    @Test
    public void testUpdateCategorySuccess() {
        String token = "validToken";
        int categoryId = 1;
        Category category = new Category();

        when(authenticationService.hasEditPermission(token)).thenReturn(true);
        when(categoryService.findById(categoryId)).thenReturn(true);

        ResponseEntity<ApiResponse> response = categoryController.updateCategory(token, categoryId, category);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("category has been updated", response.getBody().getMessage());

        verify(categoryProducer, times(1)).sendUpdateMessage(category);
    }

    @Test
    public void testUpdateCategoryNotFound() {
        String token = "validToken";
        int categoryId = 1;
        Category category = new Category();

        when(authenticationService.hasEditPermission(token)).thenReturn(true);
        when(categoryService.findById(categoryId)).thenReturn(false);

        ResponseEntity<ApiResponse> response = categoryController.updateCategory(token, categoryId, category);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("category does not exist", response.getBody().getMessage());

        verify(categoryProducer, never()).sendUpdateMessage(category);
    }

    @Test
    public void testUpdateCategoryUnauthorized() {
        String token = "invalidToken";
        int categoryId = 1;
        Category category = new Category();

        when(authenticationService.hasEditPermission(token)).thenReturn(false);

        ResponseEntity<ApiResponse> response = categoryController.updateCategory(token, categoryId, category);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("only storehouse users can update a category", response.getBody().getMessage());

        verify(categoryProducer, never()).sendUpdateMessage(category);
    }

    @Test
    public void testDeleteCategorySuccess() {
        String token = "validToken";
        Integer categoryId = 1;

        when(authenticationService.hasEditPermission(token)).thenReturn(true);

        ResponseEntity<ApiResponse> response = categoryController.deleteCategory(token, categoryId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("category has been deleted", response.getBody().getMessage());

        verify(categoryProducer, times(1)).sendDeleteMessage(categoryId.toString());
    }

    @Test
    public void testDeleteCategoryUnauthorized() {
        String token = "invalidToken";
        Integer categoryId = 1;

        when(authenticationService.hasEditPermission(token)).thenReturn(false);

        ResponseEntity<ApiResponse> response = categoryController.deleteCategory(token, categoryId);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("only storehouse users can delete a category", response.getBody().getMessage());

        verify(categoryProducer, never()).sendDeleteMessage(categoryId.toString());
    }
}
