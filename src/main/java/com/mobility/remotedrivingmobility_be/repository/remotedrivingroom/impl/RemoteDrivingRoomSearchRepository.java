package com.mobility.remotedrivingmobility_be.repository.remotedrivingroom.impl;

import com.mobility.remotedrivingmobility_be.domain.remotedrivingroom.RemoteDrivingRoom;
import com.mobility.remotedrivingmobility_be.repository.remotedrivingroom.RemoteDrivingRoomSearchableRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.mobility.remotedrivingmobility_be.domain.car.QCar.*;
import static com.mobility.remotedrivingmobility_be.domain.remotedrivingroom.QRemoteDrivingRoom.*;

@Repository
@RequiredArgsConstructor
public class RemoteDrivingRoomSearchRepository implements RemoteDrivingRoomSearchableRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<RemoteDrivingRoom> findAllByMemberId(Long memberId) {
        return queryFactory
                .selectFrom(remoteDrivingRoom)
                .where(remoteDrivingRoom.client.member.id.eq(memberId))
                .fetch();
    }

    @Override
    public RemoteDrivingRoom findByCarId(Long carId) {
        return queryFactory
                .selectFrom(remoteDrivingRoom)
                .join(remoteDrivingRoom.car, car)
                .where(remoteDrivingRoom.car.id.eq(carId))
                .fetchOne();
    }
}
