package com.mobility.remotedrivingmobility_be.domain.member;

import lombok.*;

import javax.persistence.*;

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


}
