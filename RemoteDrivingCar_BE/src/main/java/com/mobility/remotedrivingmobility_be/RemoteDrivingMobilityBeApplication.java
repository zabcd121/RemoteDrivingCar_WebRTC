package com.mobility.remotedrivingmobility_be;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import javax.persistence.EntityManager;

@SpringBootApplication
public class RemoteDrivingMobilityBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(RemoteDrivingMobilityBeApplication.class, args);
    }

    @Bean
    JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }

}
