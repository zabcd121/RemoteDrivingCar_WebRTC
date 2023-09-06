package com.mobility.remotedrivingmobility_be.repository.member;

import com.mobility.remotedrivingmobility_be.domain.member.Member;

import java.util.Optional;

public interface MemberSearchableRepository {
    Optional<Member> findByAccountLoginId(String loginId);

}
