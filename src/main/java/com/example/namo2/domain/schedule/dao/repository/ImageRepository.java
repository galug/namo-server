package com.example.namo2.domain.schedule.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.namo2.domain.schedule.domain.Image;
import com.example.namo2.domain.schedule.domain.Schedule;

public interface ImageRepository extends JpaRepository<Image, Long>, ImageRepositoryCustom {
	Optional<List<Image>> findAllBySchedule(Schedule schedule);

}
