package com.example.namo2.domain.palette;

import com.example.namo2.domain.entity.category.Palette;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaletteRepository extends JpaRepository<Palette, Long> {
}
