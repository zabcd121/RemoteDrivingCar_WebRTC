package com.mobility.remotedrivingmobility_be.repository.car;

import com.mobility.remotedrivingmobility_be.domain.car.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
