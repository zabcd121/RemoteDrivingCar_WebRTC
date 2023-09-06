package com.mobility.remotedrivingmobility_be.dto.member;

import com.mobility.remotedrivingmobility_be.domain.member.RoleType;
import lombok.*;

import static com.mobility.remotedrivingmobility_be.dto.member.TokenDto.*;

public class MemberResDto {
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Getter
    public static class LoginRes {
        private TokensDto tokenInfo;
        private LoginMemberInfoRes memberInfo;
    }

    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Getter
    public static class LoginMemberInfoRes {
        private Long memberId;
        private String loginId;
        private RoleType roleType;
    }


    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Getter @Setter
    public static class MemberSimpleInfoRes {
        private Long memberId;
        private String loginId;
    }

}
