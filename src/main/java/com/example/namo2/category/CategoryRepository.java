package com.example.namo2.category;

import com.example.namo2.entity.category.Category;
import com.example.namo2.entity.category.CategoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryDao extends JpaRepository<Category, Long> {
    public List<Category> findCategoriesByUserIdAndStatusEquals(Long userId, CategoryStatus status);
}
