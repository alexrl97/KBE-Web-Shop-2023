package com.kbe.web_shop.service;

import com.kbe.web_shop.model.Category;
import com.kbe.web_shop.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    CategoryRepo categoryRepo;
    public void createCategory(Category category) {
        categoryRepo.save(category);
    }
}
