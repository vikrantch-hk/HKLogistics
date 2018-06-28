package com.hk.logistics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hk.logistics.domain.Awb;
import com.hk.logistics.domain.AwbStatus;
import com.hk.logistics.domain.Channel;
import com.hk.logistics.domain.VendorWHCourierMapping;


/**
 * Spring Data  repository for the Awb entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AwbRepository extends JpaRepository<Awb, Long> {
	List<Awb> findByVendorWHCourierMappingAndCodAndAwbStatus(VendorWHCourierMapping vendorWHCourierMapping,Boolean cod,AwbStatus awbStatus);
	Awb findByVendorWHCourierMappingAndAwbNumber(VendorWHCourierMapping vendorWHCourierMapping,String awbNumber);
	Awb findByVendorWHCourierMappingAndAwbNumberAndCod(VendorWHCourierMapping vendorWHCourierMapping,String awbNumber,Boolean isCod);
	Awb findByAwbNumber(String awbNumber);
	Awb findByChannelAndAwbNumber(Channel channel,String awbNumber);
}
