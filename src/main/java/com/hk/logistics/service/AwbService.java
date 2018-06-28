package com.hk.logistics.service;

import java.util.List;
import java.util.Optional;

import com.hk.logistics.domain.Awb;
import com.hk.logistics.domain.AwbStatus;
import com.hk.logistics.domain.Courier;
import com.hk.logistics.domain.CourierChannel;
import com.hk.logistics.domain.VendorWHCourierMapping;
import com.hk.logistics.service.dto.AwbAttachAPIDto;
import com.hk.logistics.service.dto.AwbChangeAPIDto;
import com.hk.logistics.service.dto.AwbCourierRequest;
import com.hk.logistics.service.dto.AwbCourierResponse;
import com.hk.logistics.service.dto.AwbDTO;
import com.hk.logistics.service.dto.BrightChangeCourierRequest;
import com.hk.logistics.service.dto.CourierChangeAPIDto;

/**
 * Service Interface for managing Awb.
 */
public interface AwbService {

    /**
     * Save a awb.
     *
     * @param awbDTO the entity to save
     * @return the persisted entity
     */
    AwbDTO save(AwbDTO awbDTO);

    /**
     * Get all the awbs.
     *
     * @return the list of entities
     */
    List<AwbDTO> findAll();

    /**
     * Get the "id" awb.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<AwbDTO> findOne(Long id);

    /**
     * Delete the "id" awb.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the awb corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<AwbDTO> search(String query);
    
    Awb getAvailableAwbByVendorWHCourierMappingAndCodAndAwbStatus(VendorWHCourierMapping vendorWHCourierMapping,
			Boolean cod, AwbStatus awbStatus);

	Awb attachAwbForShipment(Courier suggestedCourier, CourierChannel courierChannel,
			VendorWHCourierMapping vendorWHCourierMapping, Boolean cod);

	Awb attachAwb(AwbAttachAPIDto awbAttachAPIDto);

	AwbCourierResponse getAwbCourierResponse(AwbCourierRequest awbCourierRequest);

	Awb changeCourier(CourierChangeAPIDto awbChaneAPIDto);

	Awb changeAwbNumber(AwbChangeAPIDto awbChangeAPIDto);

	Awb attachAwbForBright(BrightChangeCourierRequest brightChangeCourierRequest, Courier courier);

	String markAwbUnused(String courierShortCode, String awbNumber, String store, String channel, String vendorCode,
			Long warehouse);

	Awb validateAwb(Courier courier, String awbNumber, String fulfillmentCentreCode, String store, String channelName,
			String isCod);

	Awb markAwbUnused(Courier courier, String awbNumber, String fulfillmentCentreCode, String store, String channelName,
			String isCod);
}
