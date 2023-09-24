package com.kbe.web_shop.unit.controller;

import com.kbe.web_shop.config.common.ApiResponse;
import com.kbe.web_shop.controller.ProductController;
import com.kbe.web_shop.dto.product.ProductDto;
import com.kbe.web_shop.model.Category;
import com.kbe.web_shop.producer.ProductProducer;
import com.kbe.web_shop.repository.CategoryRepo;
import com.kbe.web_shop.service.AuthenticationService;
import com.kbe.web_shop.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ProductControllerUnitTest {

    @Mock
    private ProductService productService;

    @Mock
    private CategoryRepo categoryRepo;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private ProductProducer productProducer;

    @InjectMocks
    private ProductController productController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateProductSuccess() {
        String token = "validToken";
        ProductDto productDto = new ProductDto();
        productDto.setCategoryId(1);

        when(authenticationService.hasEditPermission(token)).thenReturn(true);
        when(categoryRepo.findById(productDto.getCategoryId())).thenReturn(Optional.of(new Category()));
        ResponseEntity<ApiResponse> response = productController.createProduct(token, productDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("product has been added", response.getBody().getMessage());

        verify(productProducer, times(1)).sendCreateMessage(productDto);
    }

    @Test
    public void testCreateProductCategoryNotFound() {
        String token = "validToken";
        ProductDto productDto = new ProductDto();
        productDto.setCategoryId(1);

        when(authenticationService.hasEditPermission(token)).thenReturn(true);
        when(categoryRepo.findById(productDto.getCategoryId())).thenReturn(Optional.empty());
        ResponseEntity<ApiResponse> response = productController.createProduct(token, productDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("category does not exists", response.getBody().getMessage());

        verify(productProducer, never()).sendCreateMessage(productDto);
    }

    @Test
    public void testCreateProductUnauthorized() {
        String token = "invalidToken";
        ProductDto productDto = new ProductDto();

        when(authenticationService.hasEditPermission(token)).thenReturn(false);
        ResponseEntity<ApiResponse> response = productController.createProduct(token, productDto);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("only storehouse users can create a new product", response.getBody().getMessage());

        verify(productProducer, never()).sendCreateMessage(productDto);
    }

    @Test
    public void testUpdateProductSuccess() {
        String token = "validToken";
        ProductDto productDto = new ProductDto();
        productDto.setCategoryId(1);

        when(authenticationService.hasEditPermission(token)).thenReturn(true);
        when(categoryRepo.findById(productDto.getCategoryId())).thenReturn(Optional.of(new Category()));
        ResponseEntity<ApiResponse> response = productController.updateProduct(token, productDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("product has been updated", response.getBody().getMessage());

        verify(productProducer, times(1)).sendUpdateMessage(productDto);
    }

    @Test
    public void testUpdateProductCategoryNotFound() {
        String token = "validToken";
        ProductDto productDto = new ProductDto();
        productDto.setCategoryId(1);

        when(authenticationService.hasEditPermission(token)).thenReturn(true);
        when(categoryRepo.findById(productDto.getCategoryId())).thenReturn(Optional.empty());
        ResponseEntity<ApiResponse> response = productController.updateProduct(token, productDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("category does not exists", response.getBody().getMessage());

        verify(productProducer, never()).sendUpdateMessage(productDto);
    }

    @Test
    public void testUpdateProductUnauthorized() {
        String token = "invalidToken";
        ProductDto productDto = new ProductDto();

        when(authenticationService.hasEditPermission(token)).thenReturn(false);
        ResponseEntity<ApiResponse> response = productController.updateProduct(token, productDto);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("only storehouse users can update a product", response.getBody().getMessage());

        verify(productProducer, never()).sendUpdateMessage(productDto);
    }
}