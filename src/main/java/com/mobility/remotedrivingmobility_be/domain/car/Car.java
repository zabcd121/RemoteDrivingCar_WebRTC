package com.mobility.remotedrivingmobility_be.domain.car;

import com.mobility.remotedrivingmobility_be.domain.member.Member;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Car {

    @Id @GeneratedValue
    @Column(name = "CAR_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    private String number;

    public static Car createCar(Member member, String number) {
        return Car.builder()
                .member(member)
                .number(number)
                .build();
    }
}
