package com.example.namo2.domain.category.dao.repository;

import com.example.namo2.domain.category.ui.dto.MoimCategoryDto;
import com.example.namo2.domain.category.domain.Category;
import com.example.namo2.domain.category.domain.CategoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    public List<Category> findCategoriesByUserIdAndStatusEquals(Long userId, CategoryStatus status);

    @Query(value = "select new com.example.namo2.category.dto.MoimCategoryDto(c.user.id, c.id)" +
            " from Category c" +
            " where c.user.id in :user_ids and c.name = '모임'")
    public List<MoimCategoryDto> findMoimCategoriesByUsers(@Param("user_ids") List<Long> userIds);
}
