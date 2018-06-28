package com.hk.logistics.service;

import java.util.List;

import com.hk.logistics.domain.Courier;
import com.hk.logistics.domain.CourierChannel;
import com.hk.logistics.domain.PincodeCourierMapping;
import com.hk.logistics.domain.ShipmentServiceType;
import com.hk.logistics.domain.SourceDestinationMapping;
import com.hk.logistics.domain.VendorWHCourierMapping;
import com.hk.logistics.service.dto.PincodeDeliveryInfoResponse;
import com.hk.logistics.service.dto.ReturnServiceabilityRequest;

public interface PincodeCourierService {
	
	 Integer getEstimatedDeliveryDays(List<PincodeCourierMapping> pincodeCourierMappings);

	Integer getEstimatedDeliveryDaysInfo(PincodeDeliveryInfoResponse pincodeDeliveryInfoResponse, List<SourceDestinationMapping> sourceDestinationMapping,
			String vendor,List<Long> warehouses,String channel, String store, Boolean isHkFulfilled);

	List<PincodeCourierMapping> getPincodeCourierMappingList(List<Long> warehouses,
			String channel, List<SourceDestinationMapping> sourceDestinationMapping, String vendor, String store, List<ShipmentServiceType> shipmentServiceTypes, Boolean isHkFulfilled);

	List<PincodeCourierMapping> getPincodeCourierMappingListOnShipmentServiceTypes(String channel,
			List<SourceDestinationMapping> sourceDestinationMapping, String vendor, List<ShipmentServiceType> shipmentServiceType, List<Long> warehouses, Boolean isHkFulfilled);

	Boolean checkIfReturnServiceabilityAvailable(ReturnServiceabilityRequest returnServiceabilityRequest);

	String getRoutingCode(String destinationPincode, Long warehouse, Courier courier, boolean isGroundShipped,
			boolean isCod, String channel, String sourcePincode, String vendor, String store);

	List<ShipmentServiceType> getShipmentServiceTypes(boolean isGroundShipped, boolean checkForCod,
			boolean isReversePickup, boolean checkForCardOnDelivery);

	List<VendorWHCourierMapping> getVendorWHMappings(List<Long> warehouses, String vendor,
			List<CourierChannel> courierChannels, Boolean isHkFulfilled);

	List<CourierChannel> getAllCourierListForStoreAndChannel(String storeId,String channelName);
	
}
