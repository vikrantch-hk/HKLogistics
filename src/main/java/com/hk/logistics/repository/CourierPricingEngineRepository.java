package com.hk.logistics.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.hk.logistics.domain.Courier;
import com.hk.logistics.domain.CourierPricingEngine;
import com.hk.logistics.domain.RegionType;


/**
 * Spring Data  repository for the CourierPricingEngine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourierPricingEngineRepository extends JpaRepository<CourierPricingEngine, Long>, JpaSpecificationExecutor<CourierPricingEngine> {

	CourierPricingEngine findByCourierAndRegionTypeAndValidUpto(Courier courier,RegionType regionType,Date shipmentDate);

}
