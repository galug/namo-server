package com.example.namo2.domain.category.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.namo2.domain.category.domain.Category;
import com.example.namo2.domain.category.domain.CategoryStatus;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {
	List<Category> findCategoriesByUserIdAndStatusEquals(Long userId, CategoryStatus status);
}
