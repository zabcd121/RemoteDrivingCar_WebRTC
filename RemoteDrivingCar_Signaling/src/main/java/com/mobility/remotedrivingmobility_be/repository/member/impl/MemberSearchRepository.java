package com.mobility.remotedrivingmobility_be.repository.member.impl;

import com.mobility.remotedrivingmobility_be.domain.member.Member;
import com.mobility.remotedrivingmobility_be.repository.member.MemberSearchableRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.mobility.remotedrivingmobility_be.domain.member.QMember.*;

@Repository
@RequiredArgsConstructor
public class MemberSearchRepository implements MemberSearchableRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Member> findByAccountLoginId(String loginId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(member)
                        .where(member.account.loginId.eq(loginId))
                        .fetchOne()
        );
    }
}

