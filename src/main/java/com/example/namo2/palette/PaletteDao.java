package com.example.namo2.palette;

import com.example.namo2.entity.Palette;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaletteDao extends JpaRepository<Palette, Long> {
}
