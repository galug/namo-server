package com.example.namo2.moim;

import com.example.namo2.entity.moim.Moim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MoimRepository extends JpaRepository<Moim, Long> {
    public Optional<Moim> findMoimByCode(String code);
}
