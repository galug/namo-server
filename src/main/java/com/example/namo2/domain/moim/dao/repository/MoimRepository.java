package com.example.namo2.domain.moim.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.namo2.domain.moim.domain.Moim;

public interface MoimRepository extends JpaRepository<Moim, Long>, MoimRepositoryCustom {
}
