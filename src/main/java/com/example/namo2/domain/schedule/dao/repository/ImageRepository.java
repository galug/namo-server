package com.example.namo2.domain.schedule.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.namo2.domain.schedule.domain.Image;

public interface ImageRepository extends JpaRepository<Image, Long>, ImageRepositoryCustom {
}
