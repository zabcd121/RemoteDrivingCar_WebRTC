package com.mobility.remotedrivingmobility_be.domain.member;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.*;
import static lombok.AccessLevel.*;

@Embeddable
@Builder
@Getter
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class Account {

    private String loginId;
    private String password;

    @Enumerated(STRING)
    private RoleType roleType;

    public List<String> getAuthorities() {
        List roleList = new ArrayList();

        if(!roleType.equals("")) {
            roleList.add(roleType.toString());
        }

        return roleList;
    }
}
