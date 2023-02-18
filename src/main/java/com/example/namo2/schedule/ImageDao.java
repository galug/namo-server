package com.example.namo2.schedule;

import com.example.namo2.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageDao extends JpaRepository<Image, Long> {
}
