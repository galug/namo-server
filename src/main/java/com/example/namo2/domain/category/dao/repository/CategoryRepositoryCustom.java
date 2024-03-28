package com.example.namo2.domain.category.dao.repository;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.example.namo2.domain.category.domain.Category;
import com.example.namo2.domain.category.domain.CategoryKind;

import com.example.namo2.domain.user.domain.User;

public interface CategoryRepositoryCustom {
	List<Category> findMoimCategoriesByUsers(@Param("users") List<User> users, @Param("kind") CategoryKind kind);
}
