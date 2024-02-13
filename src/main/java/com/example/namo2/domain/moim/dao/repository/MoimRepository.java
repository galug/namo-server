package com.example.namo2.domain.moim.dao.repository;

import com.example.namo2.domain.moim.domain.Moim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MoimRepository extends JpaRepository<Moim, Long> {
    Optional<Moim> findMoimByCode(String code);
}
