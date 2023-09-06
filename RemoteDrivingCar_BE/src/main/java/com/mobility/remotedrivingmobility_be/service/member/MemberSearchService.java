package com.mobility.remotedrivingmobility_be.service.member;

import com.mobility.remotedrivingmobility_be.domain.member.Member;
import com.mobility.remotedrivingmobility_be.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static com.mobility.remotedrivingmobility_be.dto.member.MemberResDto.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberSearchService {

    private final MemberRepository memberRepository;

    public MemberSimpleInfoRes searchMyPageInfo(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);

        return MemberSimpleInfoRes.builder()
                .memberId(member.getId())
                .loginId(member.getAccount().getLoginId())
                .build();
    }

}
