package com.kbe.web_shop.integration.service;

import com.kbe.web_shop.dto.product.ProductDto;
import com.kbe.web_shop.exception.ProductNotExistsException;
import com.kbe.web_shop.model.Category;
import com.kbe.web_shop.model.Product;
import com.kbe.web_shop.repository.CategoryRepo;
import com.kbe.web_shop.repository.ProductRepo;
import com.kbe.web_shop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;


    @Test
    public void testCreateProduct(){
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

        List<Product> allProducts = productRepo.findAll();

        Product createdProduct = allProducts.stream()
                .filter(p -> p.getName().equals("Test Product"))
                .findFirst()
                .orElse(null);

        assertNotNull(createdProduct);
        assertEquals("Test Product", createdProduct.getName());
        assertEquals("This is a test product.", createdProduct.getDescription());
        assertEquals("test-image.jpg", createdProduct.getImageURL());
        assertEquals(19.99, createdProduct.getPrice());
        assertEquals("12345", createdProduct.getDeckCardId());
        assertEquals("Common", createdProduct.getRarity());
    }

    @Test
    public void testGetAllProducts() {
        Category category = new Category();
        category.setCategoryName("Test Category");
        category.setId(100);
        category = categoryRepo.findFirstByOrderByIdDesc();

        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setDescription("Description 1");
        product1.setImageURL("image1.jpg");
        product1.setPrice(19.99);
        product1.setDeckCardId("12345");
        product1.setRarity("Common");
        product1.setCategory(category);
        productRepo.save(product1);

        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setDescription("Description 2");
        product2.setImageURL("image2.jpg");
        product2.setPrice(29.99);
        product2.setDeckCardId("67890");
        product2.setRarity("Rare");
        product2.setCategory(category);
        productRepo.save(product2);


        ProductDto productDto1 = productService.getAllProducts().stream().filter(X -> Objects.equals(X.getName(), "Product 1")).toList().get(0);
        ProductDto productDto2 = productService.getAllProducts().stream().filter(X -> Objects.equals(X.getName(), "Product 2")).toList().get(0);


        assertEquals("Product 1", productDto1.getName());
        assertEquals("Description 1", productDto1.getDescription());
        assertEquals("image1.jpg", productDto1.getImageURL());
        assertEquals(19.99, productDto1.getPrice());
        assertEquals("12345", productDto1.getDeckCardId());
        assertEquals("Common", productDto1.getRarity());

        assertEquals("Product 2", productDto2.getName());
        assertEquals("Description 2", productDto2.getDescription());
        assertEquals("image2.jpg", productDto2.getImageURL());
        assertEquals(29.99, productDto2.getPrice());
        assertEquals("67890", productDto2.getDeckCardId());
        assertEquals("Rare", productDto2.getRarity());
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Category category = new Category();
        category.setCategoryName("Test Category");
        categoryRepo.save(category);
        category = categoryRepo.findAll().stream().filter(X -> Objects.equals(X.getCategoryName(), "Test Category")).toList().get(0);

        Product product = new Product();
        product.setName("Original Product");
        product.setDescription("Original Description");
        product.setImageURL("original-image.jpg");
        product.setPrice(19.99);
        product.setDeckCardId("original-12345");
        product.setRarity("Original Rarity");
        product.setCategory(category);
        productRepo.save(product);
        ProductDto updatedProductDto = new ProductDto();
        updatedProductDto.setName("Updated Product");
        updatedProductDto.setDescription("Updated Description");
        updatedProductDto.setImageURL("updated-image.jpg");
        updatedProductDto.setPrice(29.99);
        updatedProductDto.setDeckCardId("updated-67890");
        updatedProductDto.setRarity("Updated Rarity");
        updatedProductDto.setCategoryId(category.getId());

        productService.updateProduct(updatedProductDto, product.getId());

        Product updatedProduct = productRepo.findById(product.getId()).orElse(null);

        assertNotNull(updatedProduct);
        assertEquals("Updated Product", updatedProduct.getName());
        assertEquals("Updated Description", updatedProduct.getDescription());
        assertEquals("updated-image.jpg", updatedProduct.getImageURL());
        assertEquals(29.99, updatedProduct.getPrice());
        assertEquals("updated-67890", updatedProduct.getDeckCardId());
        assertEquals("Updated Rarity", updatedProduct.getRarity());

        productRepo.deleteById(updatedProduct.getId());
    }

    @Test
    public void testDeleteProduct() throws Exception {
        Category category = new Category();
        category.setCategoryName("Test Category");
        categoryRepo.save(category);
        category = categoryRepo.findAll().stream().filter(X -> Objects.equals(X.getCategoryName(), "Test Category")).toList().get(0);

        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setImageURL("test-image.jpg");
        product.setPrice(19.99);
        product.setDeckCardId("test-12345");
        product.setRarity("Test Rarity");
        product.setCategory(category);
        productRepo.save(product);

        Product createdProduct = productRepo.findById(product.getId()).orElse(null);
        assertNotNull(createdProduct);

        productService.deleteProduct(product.getId());

        Product deletedProduct = productRepo.findById(product.getId()).orElse(null);

        assertNull(deletedProduct);
    }

    @Test
    public void findByIdThrowsProductNotExistsExceptionIfNotPresent(){
        assertThrows(ProductNotExistsException.class, () -> {
            productService.findById(Integer.MAX_VALUE);
        });
    }

}
