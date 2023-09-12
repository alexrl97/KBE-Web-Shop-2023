package com.kbe.web_shop.integration.service;

import com.kbe.web_shop.model.Category;
import com.kbe.web_shop.repository.CategoryRepo;
import com.kbe.web_shop.service.CategoryService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryServiceIntegrationTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepo categoryRepo;

    private Category category;

    @BeforeEach
    public void setUp(){
        category = new Category();
    }

    @Test
    public void testCreateCategory(){
        int prevSize = categoryRepo.findAll().size();
        categoryService.createCategory(category);
        int newSize = categoryRepo.findAll().size();
        assertEquals(prevSize, newSize-1);
    }

    @Test
    public void testEditCategory(){
        category.setCategoryName("Added");
        categoryService.createCategory(category);
        Category editCategory = categoryRepo.findFirstByOrderByIdDesc();
        assertEquals(editCategory.getCategoryName(), "Added");
        editCategory.setCategoryName("Edited");
        categoryService.editCategory(editCategory.getId(), editCategory);
        assertEquals(categoryRepo.findFirstByOrderByIdDesc().getCategoryName(), "Edited");
    }

    @Test
    public void testDeleteCategory(){
        categoryService.createCategory(category);
        int categoryId = categoryRepo.findFirstByOrderByIdDesc().getId();
        int prevSize = categoryRepo.findAll().size();
        categoryService.deleteCategory(categoryId);
        int newSize = categoryRepo.findAll().size();
        assertEquals(prevSize, newSize+1);
    }
}
