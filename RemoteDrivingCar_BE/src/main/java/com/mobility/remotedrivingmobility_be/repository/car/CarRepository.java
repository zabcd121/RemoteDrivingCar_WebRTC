package com.mobility.remotedrivingmobility_be.repository.car;

import com.mobility.remotedrivingmobility_be.domain.car.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("select c from Car c where c.member.id = :memberId")
    List<Car> findAllBy(Long memberId);
}
