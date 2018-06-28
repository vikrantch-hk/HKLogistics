package com.hk.logistics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.hk.logistics.domain.CourierChannel;
import com.hk.logistics.domain.VendorWHCourierMapping;


/**
 * Spring Data  repository for the VendorWHCourierMapping entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VendorWHCourierMappingRepository extends JpaRepository<VendorWHCourierMapping, Long>, JpaSpecificationExecutor<VendorWHCourierMapping> {


	List<VendorWHCourierMapping> findByVendorAndCourierChannelInAndActive(String vendor,List<CourierChannel> courierChannel,Boolean active);
	VendorWHCourierMapping findByVendorAndCourierChannelAndActive(String vendor,CourierChannel courierChannel,Boolean active);
	List<VendorWHCourierMapping> findByWarehouseInAndCourierChannelInAndActive(List<Long> warehouse,List<CourierChannel> courierChannel, boolean active);
	List<VendorWHCourierMapping> findByWarehouseAndCourierChannelInAndActive(Long warehouse,List<CourierChannel> courierChannel, boolean active);
	VendorWHCourierMapping findByWarehouseAndCourierChannelAndActive(Long warehouse,CourierChannel courierChannel, boolean active);

	VendorWHCourierMapping findByVendorAndWarehouseAndCourierChannelAndActive(String vendor,Long warehouse,CourierChannel courierChannel,Boolean active);

}
