package com.example.namo2.domain.user.dao.repository;

import com.example.namo2.domain.user.domain.Content;
import com.example.namo2.domain.user.domain.Term;
import com.example.namo2.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TermRepository extends JpaRepository<Term, Long> {
    Optional<Term> findTermByContentAndUser(Content content, User user);
}
