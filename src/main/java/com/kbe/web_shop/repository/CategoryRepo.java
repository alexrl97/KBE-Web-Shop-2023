package com.kbe.web_shop.repository;

import com.kbe.web_shop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {

    Category findByCategoryName(String categoryName);
}
