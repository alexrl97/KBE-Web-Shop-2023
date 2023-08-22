package com.kbe.web_shop.service;

import com.kbe.web_shop.dto.ProductDto;
import com.kbe.web_shop.exception.ProductNotExistsException;
import com.kbe.web_shop.model.Category;
import com.kbe.web_shop.model.Product;
import com.kbe.web_shop.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepo productRepository;

    public void createProduct(ProductDto productDto, Category category) {
        Product product = new Product();
        product.setDescription(productDto.getDescription());
        product.setImageURL(productDto.getImageURL());
        product.setName(productDto.getName());
        product.setCategory(category);
        product.setPrice(productDto.getPrice());
        product.setDeckCardId(productDto.getDeckCardId());
        product.setRarity(productDto.getRarity());
        productRepository.save(product);
    }

    public ProductDto getProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setDescription(product.getDescription());
        productDto.setImageURL(product.getImageURL());
        productDto.setName(product.getName());
        productDto.setCategoryId(product.getCategory().getId());
        productDto.setPrice(product.getPrice());
        productDto.setId(product.getId());
        productDto.setDeckCardId(product.getDeckCardId());
        productDto.setRarity(product.getRarity());
        return productDto;
    }

    public List<ProductDto> getAllProducts() {
        List<Product> allProducts = productRepository.findAll();

        List<ProductDto> productDtos = new ArrayList<>();
        for(Product product: allProducts) {
            productDtos.add(getProductDto(product));
        }
        return productDtos;
    }

    public void updateProduct(ProductDto productDto, Integer productId) throws Exception {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        // throw an exception if product does not exists
        if (optionalProduct.isEmpty()) {
            throw new Exception("product not present");
        }
        Product product = optionalProduct.get();
        product.setDescription(productDto.getDescription());
        product.setImageURL(productDto.getImageURL());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDeckCardId(productDto.getDeckCardId());
        product.setRarity(productDto.getRarity());
        productRepository.save(product);
    }

    public void deleteProduct(Integer productId) throws Exception {

        Optional<Product> optionalProduct = productRepository.findById(productId);
        // throw an exception if product does not exists
        if (optionalProduct.isEmpty()) {
            throw new Exception("product not present");
        }
        Product product = optionalProduct.get();
        productRepository.delete(product);
    }

    public Product findById(Integer productId) throws ProductNotExistsException{
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if(optionalProduct.isEmpty()){
            throw new ProductNotExistsException("product id is invalid. product id: " + productId);
        }

        return optionalProduct.get();
    }

}
