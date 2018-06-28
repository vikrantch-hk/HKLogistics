package com.hk.logistics.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hk.logistics.constants.EnumAwbStatus;
import com.hk.logistics.domain.Awb;
import com.hk.logistics.domain.AwbStatus;
import com.hk.logistics.domain.Channel;
import com.hk.logistics.domain.Courier;
import com.hk.logistics.domain.CourierChannel;
import com.hk.logistics.domain.VendorWHCourierMapping;
import com.hk.logistics.enums.EnumChannel;
import com.hk.logistics.enums.EnumWarehouse;
import com.hk.logistics.repository.AwbRepository;
import com.hk.logistics.repository.ChannelRepository;
import com.hk.logistics.repository.CourierChannelRepository;
import com.hk.logistics.repository.CourierRepository;
import com.hk.logistics.repository.PincodeRepository;
import com.hk.logistics.repository.VendorWHCourierMappingRepository;
import com.hk.logistics.repository.search.AwbSearchRepository;
import com.hk.logistics.service.AwbService;
import com.hk.logistics.service.CourierCostCalculatorService;
import com.hk.logistics.service.PincodeCourierService;
import com.hk.logistics.service.VariantService;
import com.hk.logistics.service.dto.AwbAttachAPIDto;
import com.hk.logistics.service.dto.AwbChangeAPIDto;
import com.hk.logistics.service.dto.AwbCourierRequest;
import com.hk.logistics.service.dto.AwbCourierResponse;
import com.hk.logistics.service.dto.AwbDTO;
import com.hk.logistics.service.dto.BrightChangeCourierRequest;
import com.hk.logistics.service.dto.CourierChangeAPIDto;
import com.hk.logistics.service.dto.WarehouseDTO;
import com.hk.logistics.service.mapper.AwbMapper;

/**
 * Service Implementation for managing Awb.
 */
@Service
@Transactional
public class AwbServiceImpl implements AwbService {

    private final Logger log = LoggerFactory.getLogger(AwbServiceImpl.class);

    private final AwbRepository awbRepository;

    private final AwbMapper awbMapper;

    private final AwbSearchRepository awbSearchRepository;

    public AwbServiceImpl(AwbRepository awbRepository, AwbMapper awbMapper, AwbSearchRepository awbSearchRepository) {
        this.awbRepository = awbRepository;
        this.awbMapper = awbMapper;
        this.awbSearchRepository = awbSearchRepository;
    }

    /**
     * Save a awb.
     *
     * @param awbDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AwbDTO save(AwbDTO awbDTO) {
        log.debug("Request to save Awb : {}", awbDTO);
        Awb awb = awbMapper.toEntity(awbDTO);
        awb = awbRepository.save(awb);
        AwbDTO result = awbMapper.toDto(awb);
        awbSearchRepository.save(awb);
        return result;
    }

    /**
     * Get all the awbs.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<AwbDTO> findAll() {
        log.debug("Request to get all Awbs");
        return awbRepository.findAll().stream()
            .map(awbMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one awb by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AwbDTO> findOne(Long id) {
        log.debug("Request to get Awb : {}", id);
        return awbRepository.findById(id)
            .map(awbMapper::toDto);
    }

    /**
     * Delete the awb by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Awb : {}", id);
        awbRepository.deleteById(id);
        awbSearchRepository.deleteById(id);
    }

    /**
     * Search for the awb corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<AwbDTO> search(String query) {
        log.debug("Request to search Awbs for query {}", query);
        return StreamSupport
            .stream(awbSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(awbMapper::toDto)
            .collect(Collectors.toList());
    }
    
	@Autowired
	VendorWHCourierMappingRepository vendorWHCourierMappingRepository;
	@Autowired
	AwbService awbService;
	@Autowired 
	CourierCostCalculatorService courierCostCalculatorService;
	@Autowired 
	ChannelRepository channelRepository;
	@Autowired
	CourierChannelRepository courierChannelRepository;
	@Autowired
	PincodeRepository pincodeRepository;
	@Autowired
	WarehouseService warehouseService;
	@Autowired
	VendorService vendorService;
	@Autowired
	PincodeCourierService pincodeCourierService;
	@Autowired
	CourierRepository courierRepository;
	@Autowired
	VariantService variantService;

	@Override
	public Awb getAvailableAwbByVendorWHCourierMappingAndCodAndAwbStatus(VendorWHCourierMapping vendorWHCourierMapping,Boolean cod,AwbStatus awbStatus) {
		List<Awb> awbList = awbRepository.findByVendorWHCourierMappingAndCodAndAwbStatus(vendorWHCourierMapping,cod,awbStatus);
		if (awbList != null && !awbList.isEmpty()) {
			return awbList.get(0);
		}
		return null;
	}

	@Override
	public Awb attachAwb(AwbAttachAPIDto awbAttachAPIDto){
		WarehouseDTO warehouseDTO=warehouseService.getWarehouseDTOByFulfillmentCentreCode(awbAttachAPIDto.getFulfillmentCentreCode());
		List<String> variantIds = Arrays.asList(awbAttachAPIDto.getProductVariantId().split("\\s*,\\s*"));
		for(String variantId:variantIds){
			Boolean checkSourceServiceability=variantService.checkIfProductIsServiceableAtSourcePincode(warehouseDTO.getPincode(),variantId);
		    if(!checkSourceServiceability)
			return null;
		}
		String vendor=VendorService.vendorShortCodes.get(awbAttachAPIDto.getVendorShortCode())!=null?awbAttachAPIDto.getVendorShortCode():null;
		Long warehouse=warehouseDTO.getId();
		List<Courier> couriers=courierCostCalculatorService.getBestAvailableCourierList(awbAttachAPIDto.getDestinationPincode(),null, awbAttachAPIDto.isCod(), warehouse, 
				awbAttachAPIDto.getAmount(), awbAttachAPIDto.getWeight(), awbAttachAPIDto.isGround(),awbAttachAPIDto.isCardOnDelivery(), awbAttachAPIDto.getChannel(),
				vendor, awbAttachAPIDto.getProductVariantId(), null);
		Channel channel=channelRepository.findByNameAndStore(awbAttachAPIDto.getChannel(), awbAttachAPIDto.getStore());
		CourierChannel courierChannel=courierChannelRepository.findByCourierAndChannel(couriers.get(0),channel);
		VendorWHCourierMapping vendorWHCourierMapping=vendorWHCourierMappingRepository.findByWarehouseAndCourierChannelAndActive(warehouse, courierChannel, true);
		Awb awb=attachAwbForShipment(couriers.get(0), courierChannel, vendorWHCourierMapping, awbAttachAPIDto.isCod());
		return awb;//:TODO CardoN Delivery logic
	}

	@Override
	public Awb attachAwbForShipment(Courier suggestedCourier,CourierChannel courierChannel, VendorWHCourierMapping vendorWHCourierMapping,Boolean cod) {
		Awb awb = fetchAwbForShipment(suggestedCourier,vendorWHCourierMapping,courierChannel,cod);
		if (awb != null) {
			awb.setAwbStatus(EnumAwbStatus.Attach.getAsAwbStatus());
			awbRepository.save(awb);
		}
		return awb;
	}

	@Transactional
	private Awb fetchAwbForShipment(Courier suggestedCourier, VendorWHCourierMapping vendorWHCourierMapping, CourierChannel courierChannel,Boolean cod) {
		Awb suggestedAwb;
		suggestedAwb=awbService.getAvailableAwbByVendorWHCourierMappingAndCodAndAwbStatus(vendorWHCourierMapping,cod,EnumAwbStatus.Unused.getAsAwbStatus());
		//suggestedAwb = awbService.getAvailableAwbForCourierAndChannelByWarehouseCodStatus(suggestedCourier, shippingOrder.getBaseOrder().getStore().getCourierChannel(), null, shippingOrder.getWarehouse(), isCod, EnumAwbStatus.Unused.getAsAwbStatus(), count, true);
		return suggestedAwb;
	}

	@Override
	public AwbCourierResponse getAwbCourierResponse(AwbCourierRequest awbCourierRequest) {

		List<String> variantIds = Arrays.asList(awbCourierRequest.getVariantId().split("\\s*,\\s*"));
		for(String variantId:variantIds){
			Boolean checkSourceServiceability=variantService.checkIfProductIsServiceableAtSourcePincode(awbCourierRequest.getSourcePincode(),variantId);
			if(!checkSourceServiceability)
				return null;
		}
		AwbCourierResponse awbCourierResponse = new AwbCourierResponse();
		String routingCode = null;
		Courier courier = null;
		Awb awb = null;
		Long warehouse=null;
		boolean isCod = awbCourierRequest.isCod();
		if (null != awbCourierRequest.getFulfilmentCenterCode()) {
			warehouse = warehouseService.getWarehouseCodeByFulfillmentCentreCode(awbCourierRequest.getFulfilmentCenterCode());
		} else if(WarehouseService.warehouseMap.containsKey(EnumWarehouse.HK_AQUA_WH_4_ID.getId())){
			warehouse = EnumWarehouse.HK_AQUA_WH_4_ID.getId();
		}
		boolean isGroundShipped = awbCourierRequest.isGroundShipping();
		Double totalAmount = awbCourierRequest.getAmount();
		Double estmWeight = awbCourierRequest.getWeightOfBatch();
		String vendorShortCode = awbCourierRequest.getVendor();
		boolean hkShippingProvided = true;
		Date orderPlacedDate = awbCourierRequest.getOrderDate();

		String vendor=VendorService.vendorShortCodes.get(vendorShortCode)!=null?vendorShortCode:null;
		if (awbCourierRequest.getChannel()!=null) {
			Map<Courier, Awb> courierAwbMap = courierCostCalculatorService.getCourierAwbMap(awbCourierRequest.getDestinationPincode(), awbCourierRequest.getSourcePincode(), warehouse, isGroundShipped, 
					isCod, totalAmount, estmWeight, vendor, orderPlacedDate, awbCourierRequest.getChannel(), awbCourierRequest.getVariantId(),awbCourierRequest.getStoreId());

			if (!courierAwbMap.isEmpty() && !courierAwbMap.values().isEmpty()) {
				awb = courierAwbMap.values().iterator().next();
				courier = courierAwbMap.keySet().iterator().next();
				if (courier != null) {
					routingCode=pincodeCourierService.getRoutingCode(awbCourierRequest.getDestinationPincode(), warehouse, courier, isGroundShipped, isCod,awbCourierRequest.getChannel(),
							awbCourierRequest.getSourcePincode(),vendorShortCode,
							awbCourierRequest.getStoreId().toString());
				}
			}
		}

		if (courier != null) {
			awbCourierResponse.setCourierName(courier.getName());
			awbCourierResponse.setCourierId(courier.getId());
			awbCourierResponse.setRoutingCode(routingCode);
			//awbCourierResponse.setOperationsBitset(courier.getOperationsBitset());//TODO Operation bitset response remove
			if (awbCourierRequest.getChannel().equals(EnumChannel.MP.getName())) {
				if (courier.isVendorShipping()) {
					hkShippingProvided = false;
				}
			}
			awbCourierResponse.setHkShippingProvided(hkShippingProvided);
			if (hkShippingProvided && awb != null) {
				awbCourierResponse.setAwbNumber(awb.getAwbNumber());
				awbCourierResponse.setTrackLink(awb.getTrackingLink());
			}
		} else {
			awbCourierResponse = null;
		}

		return awbCourierResponse;
	}

	@Override
	public Awb changeCourier(CourierChangeAPIDto awbChangeAPIDto) {
		//ShippingOrder shippingOrder = shipment.getShippingOrder();
		//Awb currentAwb = shipment.getAwb();
		Courier courier=courierRepository.findByShortCode(awbChangeAPIDto.getCourierShortCode());
		Channel channel=channelRepository.findByNameAndStore(awbChangeAPIDto.getChannel(), awbChangeAPIDto.getStore());
		CourierChannel courierChannel=courierChannelRepository.findByCourierAndChannel(courier, channel);
		Long warehouse=Long.parseLong(awbChangeAPIDto.getWarehouseId());
		VendorWHCourierMapping vendorWHCourierMapping=vendorWHCourierMappingRepository.findByVendorAndWarehouseAndCourierChannelAndActive(null,warehouse, courierChannel, true);
		Awb suggestedAwb = attachAwbForShipment(courier, courierChannel, vendorWHCourierMapping ,true);
		if (suggestedAwb != null) {
			//shipment.setAwb(suggestedAwb);
			//shipment = save(shipment);
			Awb awb=awbRepository.findByAwbNumber(awbChangeAPIDto.getAwbNumber());
			awb.setAwbStatus(EnumAwbStatus.Used.getAsAwbStatus());
			//getOprStatusSyncToApiService().updateShipmentAwbChanged(shipment);
			return suggestedAwb;
		}
		return null;
	}

	@Override
	public Awb changeAwbNumber(AwbChangeAPIDto awbChangeAPIDto) {
		//ShippingOrder shippingOrder = shipment.getShippingOrder();
		//Awb currentAwb = shipment.getAwb();
		Channel channel=channelRepository.findByNameAndStore(awbChangeAPIDto.getChannel(), awbChangeAPIDto.getStore());
		Courier courier=courierRepository.findByShortCode(awbChangeAPIDto.getCourierName());
		CourierChannel courierChannel=courierChannelRepository.findByCourierAndChannel(courier, channel);
		WarehouseDTO warehouse=WarehouseService.warehouseMap.get(awbChangeAPIDto.getWarehouseId());
		VendorWHCourierMapping vendorWHCourierMapping=vendorWHCourierMappingRepository.findByVendorAndWarehouseAndCourierChannelAndActive(null,warehouse.getId(), courierChannel, true);
		Awb awb=awbRepository.findByChannelAndAwbNumber(channel,awbChangeAPIDto.getNewAwbNumber());
		awb.setAwbStatus(EnumAwbStatus.Used.getAsAwbStatus());
		awbRepository.save(awb);
		Awb oldAwb=awbRepository.findByChannelAndAwbNumber(channel,awbChangeAPIDto.getNewAwbNumber());
		oldAwb.setAwbStatus(EnumAwbStatus.Used.getAsAwbStatus());
		return null;
	}

	@Override
	public Awb attachAwbForBright(BrightChangeCourierRequest brightChangeCourierRequest,Courier courier){
		if(courier==null)
			courier=courierRepository.findByShortCode(brightChangeCourierRequest.getCourierShortCode());
		Channel channel=channelRepository.findByNameAndStore(brightChangeCourierRequest.getChannel(), brightChangeCourierRequest.getStore());
		CourierChannel courierChannel=courierChannelRepository.findByCourierAndChannel(courier,channel);
		Long warehouse=warehouseService.getWarehouseCodeByFulfillmentCentreCode(brightChangeCourierRequest.getWarehouseFcCode());
		VendorWHCourierMapping vendorWHCourierMapping=vendorWHCourierMappingRepository.findByVendorAndWarehouseAndCourierChannelAndActive(null,warehouse, courierChannel, true);
		Awb awb=awbService.attachAwbForShipment(courier,courierChannel, vendorWHCourierMapping ,(Boolean)brightChangeCourierRequest.isCod());
		if(brightChangeCourierRequest.getOldAwbNumberToPreserve()!=null){
			Awb oldAwb=awbRepository.findByVendorWHCourierMappingAndAwbNumber(vendorWHCourierMapping, brightChangeCourierRequest.getOldAwbNumberToPreserve());
			oldAwb.setAwbStatus(EnumAwbStatus.Unused.getAsAwbStatus());
			awbRepository.save(oldAwb);
		}
		return awb;
	}

	@Override
	public String markAwbUnused(String courierShortCode ,String awbNumber,String store, String channel, String vendorCode, Long warehouse){
		Courier courier = courierRepository.findByShortCode(courierShortCode);
		Channel channel2=channelRepository.findByNameAndStore(channel,store);
		CourierChannel courierChannel=courierChannelRepository.findByCourierAndChannel(courier, channel2);
		VendorWHCourierMapping vendorWHCourierMapping=vendorWHCourierMappingRepository.findByVendorAndWarehouseAndCourierChannelAndActive(vendorCode,warehouse, courierChannel, true);
		String msg=null;
		if(courierChannel != null){
			Awb awb = awbRepository.findByVendorWHCourierMappingAndAwbNumber(vendorWHCourierMapping,awbNumber);
			if(awb != null && awb.getAwbStatus().equals(EnumAwbStatus.Used.getAsAwbStatus())){
				awb.setAwbStatus(EnumAwbStatus.Unused.getAsAwbStatus());
				awbRepository.save(awb);
				msg = "Awb mark unused successfully : ";
			}else {
				msg = "Awb is not in used state : ";
			}
		}else {
			msg = "Courier not found";
		}
		return msg;
	}

	@Override
	public Awb validateAwb(Courier courier ,String awbNumber, String fulfillmentCentreCode,String store,String channelName,String isCod){
		Channel channel=channelRepository.findByNameAndStore(channelName, store);
		CourierChannel courierChannel=courierChannelRepository.findByCourierAndChannel(courier,channel);
		Long warehouse=warehouseService.getWarehouseCodeByFulfillmentCentreCode(fulfillmentCentreCode);
		VendorWHCourierMapping vendorWHCourierMapping=vendorWHCourierMappingRepository.findByVendorAndWarehouseAndCourierChannelAndActive(null,warehouse, courierChannel, true);
		Awb awb = awbRepository.findByVendorWHCourierMappingAndAwbNumberAndCod(vendorWHCourierMapping,awbNumber, Boolean.parseBoolean(isCod));
		return awb;
	}

	@Override
	public Awb markAwbUnused(Courier courier ,String awbNumber, String fulfillmentCentreCode,String store,String channelName,String isCod){
		Channel channel=channelRepository.findByNameAndStore(channelName, store);
		CourierChannel courierChannel=courierChannelRepository.findByCourierAndChannel(courier,channel);
		Long warehouse=warehouseService.getWarehouseCodeByFulfillmentCentreCode(fulfillmentCentreCode);
		VendorWHCourierMapping vendorWHCourierMapping=vendorWHCourierMappingRepository.findByVendorAndWarehouseAndCourierChannelAndActive(null,warehouse, courierChannel, true);
		if(com.mysql.jdbc.StringUtils.isNullOrEmpty(isCod)){
			Awb awb = awbRepository.findByVendorWHCourierMappingAndAwbNumber(vendorWHCourierMapping,awbNumber);
			return awb;
		}
		Awb awb = awbRepository.findByVendorWHCourierMappingAndAwbNumberAndCod(vendorWHCourierMapping,awbNumber, Boolean.parseBoolean(isCod));
		return awb;
	}
}
