package com.example.namo2.moim;

import com.example.namo2.entity.moimmemo.MoimMemoLocation;
import com.example.namo2.entity.moimmemo.MoimMemoLocationAndUser;
import com.example.namo2.entity.moimmemo.MoimMemoLocationImg;
import com.example.namo2.entity.moimmemo.QMoimMemoLocationAndUser;
import com.example.namo2.moim.dto.MoimMemoLocationDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.namo2.entity.moimmemo.QMoimMemoLocation.moimMemoLocation;
import static com.example.namo2.entity.moimmemo.QMoimMemoLocationAndUser.moimMemoLocationAndUser;

public class MoimMemoLocationRepositoryImpl implements MoimMemoLocationRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public MoimMemoLocationRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public List<MoimMemoLocationDto> findMoimMemo(Long moimScheduleId) {
        List<MoimMemoLocation> moimMemoLocations = queryFactory.select(moimMemoLocation).distinct()
                .from(moimMemoLocation)
                .join(moimMemoLocation.moimMemo).fetchJoin()
                .join(moimMemoLocation.moimMemoLocationImgs).fetchJoin()
                .where(moimMemoLocation.moimMemo.moimSchedule.id.eq(moimScheduleId))
                .fetch();

        List<MoimMemoLocationDto> moimMemoLocationDtos = moimMemoLocations.stream()
                .map(moimMemoLocation -> new MoimMemoLocationDto(moimMemoLocation.getId(),
                        moimMemoLocation.getName(), moimMemoLocation.getTotalAmount(),
                        moimMemoLocation.getMoimMemoLocationImgs().stream().map(MoimMemoLocationImg::getUrl).collect(Collectors.toList())))
                .collect(Collectors.toList());
        List<Long> moimMemoLocationIds = moimMemoLocationDtos.stream().map(MoimMemoLocationDto::getMoimMemoLocationId)
                .collect(Collectors.toList());

        Map<Long, List<MoimMemoLocationAndUser>> locationAndUsersMap = queryFactory.select(moimMemoLocationAndUser)
                .from(moimMemoLocationAndUser)
                .where(moimMemoLocationAndUser.moimMemoLocation.id.in(moimMemoLocationIds))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(moimMemoLocationAndUser -> moimMemoLocationAndUser.getMoimMemoLocation().getId()));
        moimMemoLocationDtos.stream()
                .forEach((moimMemoLocationDto -> moimMemoLocationDto.addLocationParticipants(locationAndUsersMap.get(moimMemoLocationDto.getMoimMemoLocationId()))));
        return moimMemoLocationDtos;
    }
}
