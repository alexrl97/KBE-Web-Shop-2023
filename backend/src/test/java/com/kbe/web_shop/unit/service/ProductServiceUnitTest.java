package com.kbe.web_shop.unit.service;
import com.kbe.web_shop.dto.product.ProductDto;
import com.kbe.web_shop.exception.ProductNotExistsException;
import com.kbe.web_shop.model.Category;
import com.kbe.web_shop.model.Product;
import com.kbe.web_shop.repository.CategoryRepo;
import com.kbe.web_shop.repository.ProductRepo;
import com.kbe.web_shop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceUnitTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepo productRepository;

    @Mock
    private CategoryRepo categoryRepo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateProduct() {
        ProductDto productDto = new ProductDto();
        productDto.setName("Product Name");
        productDto.setImageURL("image.jpg");
        productDto.setPrice(10.0);
        productDto.setDescription("Product Description");
        productDto.setCategoryId(1);

        Category category = new Category();
        category.setId(1);

        productService.createProduct(productDto, category);

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testGetProductDto() {
        Product product = new Product();
        product.setId(1);
        product.setName("Product Name");
        product.setImageURL("image.jpg");
        product.setPrice(10.0);
        product.setDescription("Product Description");
        Category category = new Category();
        category.setId(1);
        product.setCategory(category);

        ProductDto productDto = productService.getProductDto(product);

        assertNotNull(productDto);
        assertEquals(product.getId(), productDto.getId());
        assertEquals(product.getName(), productDto.getName());
        assertEquals(product.getImageURL(), productDto.getImageURL());
        assertEquals(product.getPrice(), productDto.getPrice());
        assertEquals(product.getDescription(), productDto.getDescription());
        assertEquals(product.getCategory().getId(), productDto.getCategoryId());
    }

    @Test
    public void testGetAllProducts() {
        Product product1 = new Product();
        product1.setId(1);
        product1.setName("Product 1");
        product1.setImageURL("image1.jpg");
        product1.setPrice(10.0);
        product1.setDescription("Description 1");
        Category category1 = new Category();
        category1.setId(1);
        product1.setCategory(category1);

        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Product 2");
        product2.setImageURL("image2.jpg");
        product2.setPrice(15.0);
        product2.setDescription("Description 2");
        Category category2 = new Category();
        category2.setId(2);
        product2.setCategory(category2);

        List<Product> allProducts = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(allProducts);

        List<ProductDto> productDtos = productService.getAllProducts();

        assertNotNull(productDtos);
        assertEquals(2, productDtos.size());
    }

    @Test
    public void testUpdateProduct() {
        ProductDto productDto = new ProductDto();
        productDto.setName("Updated Name");
        productDto.setImageURL("updated.jpg");
        productDto.setPrice(20.0);
        productDto.setDescription("Updated Description");
        productDto.setCategoryId(1);

        Integer id = 1;

        Product product = new Product();
        product.setId(id);
        Category category = new Category();
        category.setId(id);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(categoryRepo.findById(id)).thenReturn(Optional.of(category));

        assertDoesNotThrow(() -> productService.updateProduct(productDto, id));
    }

    @Test
    public void testUpdateProductWithInvalidProductId() {
        ProductDto productDto = new ProductDto();
        Integer productId = 1;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> productService.updateProduct(productDto, productId));
    }

    @Test
    public void testDeleteProduct() {
        Integer productId = 1;

        Product product = new Product();
        product.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertDoesNotThrow(() -> productService.deleteProduct(productId));
    }

    @Test
    public void testDeleteProductWithInvalidProductId() {
        Integer productId = 1;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> productService.deleteProduct(productId));
    }

    @Test
    public void testFindById() {
        Integer productId = 1;
        Product product = new Product();
        product.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        try {
            Product foundProduct = productService.findById(productId);
            assertNotNull(foundProduct);
            assertEquals(productId, foundProduct.getId());
        } catch (ProductNotExistsException e) {
            fail("ProductNotExistsException should not be thrown.");
        }
    }

    @Test
    public void testFindByIdWithInvalidProductId() {
        Integer productId = 1;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotExistsException.class, () -> productService.findById(productId));
    }
}
