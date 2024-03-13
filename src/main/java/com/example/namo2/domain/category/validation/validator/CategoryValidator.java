package com.example.namo2.domain.category.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.example.namo2.domain.category.validation.annotation.CategoryName;

public class CategoryValidator implements ConstraintValidator<CategoryName, String> {

	public static final String BASE_SCHEDULE_CATEGORY = "일정";
	public static final String BASE_MOIM_CATEGORY = "모임";

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.isBlank()) {
			return false;
		}
		if (value.equals(BASE_SCHEDULE_CATEGORY) || value.equals(BASE_MOIM_CATEGORY)) {
			return false;
		}
		return true;
	}
}
