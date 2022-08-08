package com.udacity.pricing.domain.price;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PriceRepository
 * This repository provide a type of data persistence while the web service runs,
 * namely the ID->price pairing generated by the PricingService.
 *
 * http://localhost:8082/prices/search/findByVehicleId?vehicleId=22
 */
@Repository
public interface PriceRepository extends CrudRepository<Price, Long> {
    List<Price> findByVehicleId(@Param("vehicleId") Long vehicleId);

}
