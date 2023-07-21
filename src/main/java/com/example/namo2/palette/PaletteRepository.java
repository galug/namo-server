package com.example.namo2.palette;

import com.example.namo2.entity.category.Palette;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaletteDao extends JpaRepository<Palette, Long> {
}
