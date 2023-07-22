package com.example.namo2.schedule;

import com.example.namo2.entity.schedule.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long>, ImageRepositoryCustom {
}
