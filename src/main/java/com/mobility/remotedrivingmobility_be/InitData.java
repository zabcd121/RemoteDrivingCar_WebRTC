package com.mobility.remotedrivingmobility_be;


import com.mobility.remotedrivingmobility_be.domain.car.Car;
import com.mobility.remotedrivingmobility_be.domain.member.Account;
import com.mobility.remotedrivingmobility_be.domain.member.Member;
import com.mobility.remotedrivingmobility_be.domain.member.RoleType;
import com.mobility.remotedrivingmobility_be.domain.remotedrivingroom.RemoteDrivingRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitData {

    private final InitService initService;

    @PostConstruct
    public void init() throws Exception {
        //initService.adminInit();
        //initService.dataInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        private final PasswordEncoder passwordEncoder;

        public void adminInit() {
            Member admin1 = Member.builder()
                    .account(Account.builder().loginId("kitadmin1").password(passwordEncoder.encode("admin12345!")).roleType(RoleType.ROLE_ADMIN).build())
                    .build();
            Member admin2 = Member.builder()
                    .account(Account.builder().loginId("kitadmin2").password(passwordEncoder.encode("admin12345!")).roleType(RoleType.ROLE_ADMIN).build())
                    .build();
            em.persist(admin1);
            em.persist(admin2);
        }

        public void dataInit() throws Exception {
            Member member1 = Member.builder()
                    .account(Account.builder().loginId("khs12345").password(passwordEncoder.encode("khs12345!")).roleType(RoleType.ROLE_MEMBER).build())
                    .build();
            Member member2 = Member.builder()
                    .account(Account.builder().loginId("ahn12345").password(passwordEncoder.encode("ahn12345!")).roleType(RoleType.ROLE_MEMBER).build())
                    .build();

            em.persist(member1);
            em.persist(member2);

            Car car1 = Car.builder()
                    .member(member1)
                    .number("13머 1234")
                    .build();

            Car car2 = Car.builder()
                    .member(member1)
                    .number("21서 5678")
                    .build();
            em.persist(car1);
            em.persist(car2);

            RemoteDrivingRoom remoteDrivingRoom1 = RemoteDrivingRoom.createRemoteDrivingRoom(car1);
            RemoteDrivingRoom remoteDrivingRoom2 = RemoteDrivingRoom.createRemoteDrivingRoom(car2);
            em.persist(remoteDrivingRoom1);
            em.persist(remoteDrivingRoom2);

        }

    }
}
