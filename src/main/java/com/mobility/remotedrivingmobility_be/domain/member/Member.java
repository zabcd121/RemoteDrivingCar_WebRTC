package com.mobility.remotedrivingmobility_be.domain.member;

import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.*;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Embedded
    private Account account;

    private String name;

    public static Member createMember(String loginId, String encodedPW, String name, RoleType role) {
        Member member = Member.builder()
                .account(Account.builder()
                        .loginId(loginId)
                        .password(encodedPW)
                        .roleType(role)
                        .build())
                .name(name)
                .build();

        return member;
    }

}
