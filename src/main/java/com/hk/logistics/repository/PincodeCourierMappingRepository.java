package com.hk.logistics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.hk.logistics.domain.PincodeCourierMapping;
import com.hk.logistics.domain.SourceDestinationMapping;
import com.hk.logistics.domain.VendorWHCourierMapping;


/**
 * Spring Data  repository for the PincodeCourierMapping entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PincodeCourierMappingRepository extends JpaRepository<PincodeCourierMapping, Long>,JpaSpecificationExecutor<PincodeCourierMapping> {

	List<PincodeCourierMapping> findBySourceDestinationMappingInAndVendorWHCourierMappingIn(List<SourceDestinationMapping> sourceDestinationMapping,
			List<VendorWHCourierMapping> vendorWHCourierMapping);
	
	List<PincodeCourierMapping> findBySourceDestinationMappingAndVendorWHCourierMappingIn(List<SourceDestinationMapping> sourceDestinationMapping,
			List<VendorWHCourierMapping> vendorWHCourierMapping);
}
