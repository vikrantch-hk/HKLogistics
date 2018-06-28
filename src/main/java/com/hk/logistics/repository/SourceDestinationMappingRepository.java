package com.hk.logistics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.hk.logistics.domain.SourceDestinationMapping;


/**
 * Spring Data  repository for the SourceDestinationMapping entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SourceDestinationMappingRepository extends JpaRepository<SourceDestinationMapping, Long>, JpaSpecificationExecutor<SourceDestinationMapping> {

	List<SourceDestinationMapping> findBySourcePincodeInAndDestinationPincode(List<String> sourcePincodes,String destinationPincode);

	SourceDestinationMapping findBySourcePincodeAndDestinationPincode(String sourcePincode,String destinationPincode);
}
