package com.example.namo2.category;

import com.example.namo2.category.dto.MoimCategoryDto;
import com.example.namo2.entity.category.Category;
import com.example.namo2.entity.category.CategoryStatus;
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
