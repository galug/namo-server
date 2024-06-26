package com.example.namo2.domain.group.dao.repository.group;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.namo2.domain.group.domain.Moim;
import com.example.namo2.domain.group.domain.MoimAndUser;

import com.example.namo2.domain.user.domain.User;

public interface MoimAndUserRepository extends JpaRepository<MoimAndUser, Long> {

	@Query(value = "select gu from MoimAndUser gu join fetch gu.moim where gu.user= :user order by gu.createdDate")
	List<MoimAndUser> findMoimAndUserByUser(User user);

	@Query(value = "select gu from MoimAndUser gu join fetch gu.user where gu.moim= :moim")
	List<MoimAndUser> findMoimAndUserByMoim(Moim moim);

	@Query(value = "select gu from MoimAndUser gu join fetch gu.user where gu.moim in :moims")
	List<MoimAndUser> findMoimAndUserByMoim(List<Moim> moims);

	Optional<MoimAndUser> findMoimAndUserByUserAndMoim(User user, Moim moim);

	void deleteAllByUser(User user);
}
