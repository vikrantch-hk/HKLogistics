package com.hk.logistics.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hk.logistics.domain.CourierGroup;
import com.hk.logistics.domain.PincodeRegionZone;
import com.hk.logistics.domain.SourceDestinationMapping;


/**
 * Spring Data  repository for the PincodeRegionZone entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PincodeRegionZoneRepository extends JpaRepository<PincodeRegionZone, Long> {

	List<PincodeRegionZone> findBySourceDestinationMappingAndCourierGroupIn(SourceDestinationMapping sourceDestinationMapping,Set<CourierGroup> courierGroups);

	PincodeRegionZone findBySourceDestinationMappingAndCourierGroup(SourceDestinationMapping sourceDestinationMapping,CourierGroup courierGroup);
}
