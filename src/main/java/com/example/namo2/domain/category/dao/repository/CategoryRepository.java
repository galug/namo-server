package com.example.namo2.domain.category.dao.repository;

import com.example.namo2.domain.category.ui.dto.MoimCategoryDto;
import com.example.namo2.domain.category.domain.Category;
import com.example.namo2.domain.category.domain.CategoryStatus;
import com.example.namo2.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    public List<Category> findCategoriesByUserIdAndStatusEquals(Long userId, CategoryStatus status);

    @Query(value = "select c" +
            " from Category c" +
            " join fetch c.user" +
            " where c.user in :users and c.name = '모임'")
    List<Category> findMoimCategoriesByUsers(@Param("users") List<User> users);
}
