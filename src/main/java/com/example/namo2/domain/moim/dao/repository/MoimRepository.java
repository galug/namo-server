package com.example.namo2.domain.moim.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.namo2.domain.moim.domain.Moim;

public interface MoimRepository extends JpaRepository<Moim, Long>, MoimRepositoryCustom {
	@Query("select m from Moim m join m.moimAndUsers where m.id = :moimId")
	Optional<Moim> findMoimWithMoimAndUsersByMoimId(Long moimId);
}
