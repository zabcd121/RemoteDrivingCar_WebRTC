package com.mobility.remotedrivingmobility_be.domain.car;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

//@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Embeddable
public class Image {

    @Column(length = 50)
    private String fileLocalName;

    @Column(length = 50)
    private String fileOriName;

    @Column(length = 50)
    private String fileUrl;
}
