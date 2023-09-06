package com.mobility.remotedrivingmobility_be.repository.member;

import com.mobility.remotedrivingmobility_be.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SignupRepository extends JpaRepository<Member, Long>{


    boolean existsByAccountLoginId(String loginId);
}
