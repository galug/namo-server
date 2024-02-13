package com.example.namo2.domain.memo;

import com.example.namo2.domain.memo.domain.MoimMemoLocation;
import com.example.namo2.domain.memo.domain.MoimMemoLocationAndUser;
import com.example.namo2.domain.memo.domain.MoimMemoLocationImg;
import com.example.namo2.domain.moim.ui.dto.MoimMemoLocationDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.namo2.domain.memo.domain.QMoimMemoLocation.moimMemoLocation;
import static com.example.namo2.domain.memo.domain.QMoimMemoLocationAndUser.moimMemoLocationAndUser;

public class MoimMemoLocationRepositoryImpl implements MoimMemoLocationRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public MoimMemoLocationRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MoimMemoLocationDto> findMoimMemo(Long moimScheduleId) {
        List<MoimMemoLocation> moimMemoLocations = queryFactory.select(moimMemoLocation).distinct()
                .from(moimMemoLocation)
                .join(moimMemoLocation.moimMemo).fetchJoin()
                .leftJoin(moimMemoLocation.moimMemoLocationImgs).fetchJoin()
                .where(moimMemoLocation.moimMemo.moimSchedule.id.eq(moimScheduleId))
                .fetch();

        List<MoimMemoLocationDto> moimMemoLocationDtos = moimMemoLocations.stream()
                .map(moimMemoLocation -> new MoimMemoLocationDto(moimMemoLocation.getId(),
                        moimMemoLocation.getName(), moimMemoLocation.getTotalAmount(),
                        moimMemoLocation.getMoimMemoLocationImgs().stream().map(MoimMemoLocationImg::getUrl).collect(Collectors.toList())))
                .collect(Collectors.toList());
        List<Long> moimMemoLocationIds = moimMemoLocationDtos.stream()
                .map(MoimMemoLocationDto::getMoimMemoLocationId)
                .collect(Collectors.toList());

        Map<Long, List<MoimMemoLocationAndUser>> locationAndUsersMap = queryFactory.select(moimMemoLocationAndUser)
                .from(moimMemoLocationAndUser)
                .where(moimMemoLocationAndUser.moimMemoLocation.id.in(moimMemoLocationIds))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(moimMemoLocationAndUser -> moimMemoLocationAndUser.getMoimMemoLocation().getId()));
        moimMemoLocationDtos
                .forEach((moimMemoLocationDto -> moimMemoLocationDto.addLocationParticipants(locationAndUsersMap.get(moimMemoLocationDto.getMoimMemoLocationId()))));
        return moimMemoLocationDtos;
    }
}
