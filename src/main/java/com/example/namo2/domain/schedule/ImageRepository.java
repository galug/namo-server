package com.example.namo2.domain.schedule;

import com.example.namo2.domain.schedule.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long>, ImageRepositoryCustom {
}
