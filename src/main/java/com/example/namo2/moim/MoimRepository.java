package com.example.namo2.moim;

import com.example.namo2.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoimRepository extends JpaRepository<Group, Long> {
}
