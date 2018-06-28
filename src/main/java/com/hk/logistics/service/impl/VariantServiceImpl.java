package com.hk.logistics.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.logistics.domain.Pincode;
import com.hk.logistics.domain.PincodeCourierMapping;
import com.hk.logistics.domain.ProductVariant;
import com.hk.logistics.domain.ShipmentServiceType;
import com.hk.logistics.domain.SourceDestinationMapping;
import com.hk.logistics.repository.ChannelRepository;
import com.hk.logistics.repository.CourierChannelRepository;
import com.hk.logistics.repository.PincodeCourierMappingRepository;
import com.hk.logistics.repository.PincodeRepository;
import com.hk.logistics.repository.ProductVariantRepository;
import com.hk.logistics.repository.SourceDestinationMappingRepository;
import com.hk.logistics.repository.VendorWHCourierMappingRepository;
import com.hk.logistics.service.PincodeCourierService;
import com.hk.logistics.service.VariantService;
import com.hk.logistics.service.dto.MessageConstants;
import com.hk.logistics.service.dto.PincodeDeliveryInfoRequest;
import com.hk.logistics.service.dto.PincodeDeliveryInfoResponse;
import com.hk.logistics.service.dto.ProductVariantDTO;
import com.hk.logistics.service.dto.ServiceabilityApiDTO;
import com.hk.logistics.service.dto.StoreConstants;
import com.hk.logistics.service.dto.StoreVariantAPIObj;
import com.hk.logistics.service.dto.VariantServiceabilityRequest;
import com.hk.logistics.service.dto.WarehouseDTO;

@Service
public class VariantServiceImpl implements VariantService{


	@Autowired
	PincodeRepository pincodeRepository;
	@Autowired
	VendorService vendorService;
	@Autowired
	CourierChannelRepository courierChannelRepository;
	@Autowired
	VendorWHCourierMappingRepository vendorWhCourierMappingRepository;
	@Autowired 
	SourceDestinationMappingRepository sourceDestinationMappingRepository;
	@Autowired
	PincodeCourierMappingRepository pincodeCourierMappingRepository;
	@Autowired
	PincodeCourierService pincodeCourierService;
	@Autowired
	WarehouseService warehouseService;
	@Autowired
	ChannelRepository channelRepository;
	@Autowired
	ProductVariantRepository productVariantRepository;

	public static volatile Map<String, ProductVariantDTO> productVariantMap=new HashMap<>();

	@Override
	public Boolean checkIfProductIsServiceableAtSourcePincode(String pincode,String productVariantId){
		ProductVariant productVariant=productVariantRepository.findByVariantIdAndPincode(productVariantId, pincode);
		if(productVariant!=null && !productVariant.isServiceable()){
			return false;
		}
		return true;
	}

	@Override
	public PincodeDeliveryInfoResponse getVariantDeliveryInfoByPincode(PincodeDeliveryInfoRequest pincodeDeliveryInfoRequest) {

		PincodeDeliveryInfoResponse pincodeDeliveryInfoResponse=new PincodeDeliveryInfoResponse(StoreConstants.DEFAULT_STORE_ID);
		validatePincodeDeliverInfoRequest(pincodeDeliveryInfoRequest, pincodeDeliveryInfoResponse);
		if(pincodeDeliveryInfoResponse.isException()){
			return pincodeDeliveryInfoResponse;
		}
		Pincode destinationPincode=pincodeRepository.findByPincode(pincodeDeliveryInfoRequest.getDestinationPincode());
		validateDestinationPincode(pincodeDeliveryInfoResponse, destinationPincode);
		if(pincodeDeliveryInfoResponse.isException()){
			return pincodeDeliveryInfoResponse;
		}

		ServiceabilityApiDTO svapiObj=pincodeDeliveryInfoRequest.getSvApiObj();
		List<String> finalsourcePincodesList=new ArrayList<String>();
		List<Long> warehouses=new ArrayList<>();
		if(svapiObj.isHkFulfilled()){
			for(String locationCode:svapiObj.getFulfillmentCentreCodes()){
				WarehouseDTO warehouseDTO=warehouseService.getWarehouseDTOByFulfillmentCentreCode(locationCode);
				if(warehouseDTO!=null){
					if (svapiObj.getLocationCodes().contains(warehouseDTO.getId())) {
						warehouses.add(Long.parseLong(locationCode));
					}
					String pincode=warehouseDTO.getPincode();
					Boolean checkSourceServiceability=checkIfProductIsServiceableAtSourcePincode(warehouseDTO.getPincode(),svapiObj.getProductVariantId());
					if(checkSourceServiceability){
						finalsourcePincodesList.add(pincode);
					}
				}
			}
		}
		List<SourceDestinationMapping> sourceDestinationMapping=sourceDestinationMappingRepository.findBySourcePincodeInAndDestinationPincode(finalsourcePincodesList, destinationPincode.getPincode());
		String vendor=VendorService.vendorShortCodes.get(svapiObj.getVendorShortCode())!=null?svapiObj.getVendorShortCode():null;

		String store=pincodeDeliveryInfoRequest.getStoreId().toString();
		List<ShipmentServiceType> shipmentServiceTypes=pincodeCourierService.getShipmentServiceTypes(svapiObj.isGroundShipped(),false,false,false);
		Integer estimatedDeliveryDays=pincodeCourierService.getEstimatedDeliveryDaysInfo(pincodeDeliveryInfoResponse, sourceDestinationMapping,
				vendor,warehouses,svapiObj.getCourierChannel(), store, svapiObj.isHkFulfilled());

		if(estimatedDeliveryDays==null){
			pincodeDeliveryInfoResponse.addMessage(MessageConstants.COURIER_SERVICE_NOT_AVAILABLE);
			pincodeDeliveryInfoResponse.setException(true);
		}
		shipmentServiceTypes.clear();
		shipmentServiceTypes=pincodeCourierService.getShipmentServiceTypes(svapiObj.isGroundShipped(),true,false,false);//To check Cod
		List<PincodeCourierMapping> pincodeCourierMappings=pincodeCourierService.getPincodeCourierMappingListOnShipmentServiceTypes(svapiObj.getCourierChannel(),sourceDestinationMapping, vendor,
				shipmentServiceTypes, warehouses, svapiObj.isHkFulfilled());
		if(pincodeCourierMappings!=null){
			pincodeDeliveryInfoResponse.setEstmDeliveryDays(estimatedDeliveryDays);
			pincodeDeliveryInfoResponse.setCodAllowed(true);
		}
		if(svapiObj.isVendorShipping() || svapiObj.isVendorShippingCod()) {
			if (!svapiObj.isVendorShippingCod() ) {
				pincodeDeliveryInfoResponse.setCodAllowed(false);
			}
		}
		return null;
	}


	public PincodeDeliveryInfoResponse validateDestinationPincode(PincodeDeliveryInfoResponse pincodeDeliveryInfoResponse,
			Pincode destinationPincode) {
		if(destinationPincode==null){
			pincodeDeliveryInfoResponse.addMessage(MessageConstants.SERVICE_NOT_AVAILABLE_ON_PINCODE);
			pincodeDeliveryInfoResponse.setException(true);
		}
		return pincodeDeliveryInfoResponse;
	}

	public PincodeDeliveryInfoResponse validatePincodeDeliverInfoRequest(PincodeDeliveryInfoRequest pincodeDeliveryInfoRequest,PincodeDeliveryInfoResponse pincodeDeliveryInfoResponse){
		if(pincodeDeliveryInfoRequest==null || pincodeDeliveryInfoRequest.getStoreId()==null ){
			pincodeDeliveryInfoResponse.addMessage(MessageConstants.REQ_PARAMETERS_INVALID);
			pincodeDeliveryInfoResponse.setException(true);
			return pincodeDeliveryInfoResponse;
		}
		//TODO : add store entity null check 
		ServiceabilityApiDTO serviceabilityApiDTO=pincodeDeliveryInfoRequest.getSvApiObj();
		if(serviceabilityApiDTO==null || serviceabilityApiDTO.getProductVariantId()==null || pincodeDeliveryInfoRequest.getDestinationPincode()==null){
			pincodeDeliveryInfoResponse.addMessage(MessageConstants.REQ_PARAMETERS_INVALID);
			pincodeDeliveryInfoResponse.setException(true);
			return pincodeDeliveryInfoResponse;
		}
		if(serviceabilityApiDTO.getCourierChannel()==null){
			pincodeDeliveryInfoResponse.addMessage(MessageConstants.COURIER_CHANNEL_NOT_AVAILABLE);
			pincodeDeliveryInfoResponse.setException(true);
			return pincodeDeliveryInfoResponse;
		}

		return pincodeDeliveryInfoResponse;

	}

	@Override
	public StoreVariantAPIObj getVariantServiceabilityDetails(VariantServiceabilityRequest variantServiceabilityRequest) {

		StoreVariantAPIObj svObj=variantServiceabilityRequest.getSvObj();
		String channel=variantServiceabilityRequest.getChannel();
		Pincode destinationPincode=pincodeRepository.findByPincode(variantServiceabilityRequest.getDestinationPincode());
		String vendorCode=variantServiceabilityRequest.getVendorCode();
		List<Long> warehouseList=variantServiceabilityRequest.getWarehouseList(); 
		boolean isGroundShipped =variantServiceabilityRequest.isGroundShipped();
		if (destinationPincode != null && (vendorCode!=null || (warehouseList != null && warehouseList.size() > 0))) {
			boolean shippable = false;
			List<SourceDestinationMapping> sourceDestinationMapping=sourceDestinationMappingRepository.findBySourcePincodeInAndDestinationPincode(null, destinationPincode.getPincode());
			String vendor=VendorService.vendorShortCodes.get(svObj.getVendorShortCode())!=null?svObj.getVendorShortCode():null;

			List<ShipmentServiceType> shipmentServiceTypes=pincodeCourierService.getShipmentServiceTypes(isGroundShipped,true,false,false);//To check Cod
			List<PincodeCourierMapping> pincodeCourierMappings=pincodeCourierService.getPincodeCourierMappingList(warehouseList,variantServiceabilityRequest.getChannel(),sourceDestinationMapping, vendor,
					variantServiceabilityRequest.getStore(),shipmentServiceTypes, variantServiceabilityRequest.isHkFulfilled());
			if(pincodeCourierMappings!=null && pincodeCourierMappings.size()>0){
				shippable=true;
			}
			//checking ship-ability
			svObj.setShippable(shippable);
			// Checking cod
			boolean isCodAllowed = false;
			shipmentServiceTypes.clear();
			shipmentServiceTypes=pincodeCourierService.getShipmentServiceTypes(isGroundShipped,true,false,false);
			List<PincodeCourierMapping> pincodeCourierMappingsForCOD=pincodeCourierService.getPincodeCourierMappingListOnShipmentServiceTypes(channel,sourceDestinationMapping, vendor,shipmentServiceTypes, 
					null, variantServiceabilityRequest.isHkFulfilled());
			if(pincodeCourierMappingsForCOD!=null && pincodeCourierMappingsForCOD.size()>0){
				isCodAllowed=true;
			}
			if(svObj.isVendorShipping()) {
				if (!svObj.isVendorShippingCod()) {
					isCodAllowed = false;
				}
			}
			svObj.setCodAllowed(isCodAllowed);
			
			// Checking cardOnDelivery
			if(!variantServiceabilityRequest.isHkFulfilled()) {
				//serviceTypeList = pincodeCourierService.getShipmentServiceType(isGroundShipped, false, false, true);
				shipmentServiceTypes.clear();
				shipmentServiceTypes=pincodeCourierService.getShipmentServiceTypes(isGroundShipped,false,false,true);
				List<PincodeCourierMapping> pincodeCourierMappingsForCardOnDelivery=pincodeCourierService.getPincodeCourierMappingListOnShipmentServiceTypes(channel,sourceDestinationMapping,
						vendor,shipmentServiceTypes, null, null);
				if(pincodeCourierMappingsForCardOnDelivery!=null && pincodeCourierMappingsForCardOnDelivery.size()!=0)	       
					svObj.setCardOnDeliveryAllowed(true);
			}
		}
		return svObj;
	}

}
