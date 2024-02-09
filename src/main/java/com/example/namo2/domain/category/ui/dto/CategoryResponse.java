package com.example.namo2.domain.category.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class CategoryResponse {

    private CategoryResponse() {
        throw new IllegalStateException("Utility class");
    }

    @AllArgsConstructor
    @Getter
    public static class CategoryIdDto {
        private Long id;
    }

    @AllArgsConstructor
    @Getter
    public static class CategoryDto {
        private Long categoryId;
        private String name;
        private Long paletteId;
        private Boolean isShare;
    }
}
