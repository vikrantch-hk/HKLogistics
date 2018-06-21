package com.hk.logistics.web.rest;

import com.hk.logistics.HkLogisticsApp;

import com.hk.logistics.domain.PincodeCourierMapping;
import com.hk.logistics.repository.PincodeCourierMappingRepository;
import com.hk.logistics.repository.search.PincodeCourierMappingSearchRepository;
import com.hk.logistics.service.PincodeCourierMappingService;
import com.hk.logistics.service.dto.PincodeCourierMappingDTO;
import com.hk.logistics.service.mapper.PincodeCourierMappingMapper;
import com.hk.logistics.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.hk.logistics.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PincodeCourierMappingResource REST controller.
 *
 * @see PincodeCourierMappingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HkLogisticsApp.class)
public class PincodeCourierMappingResourceIntTest {

    private static final String DEFAULT_ROUTING_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ROUTING_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_APPLICABLE_FOR_CHEAPEST_COURIER = false;
    private static final Boolean UPDATED_APPLICABLE_FOR_CHEAPEST_COURIER = true;

    private static final Double DEFAULT_ESTIMATED_DELIVERY_DAYS = 1D;
    private static final Double UPDATED_ESTIMATED_DELIVERY_DAYS = 2D;

    private static final Boolean DEFAULT_PICKUP_AVAILABLE = false;
    private static final Boolean UPDATED_PICKUP_AVAILABLE = true;

    private static final Boolean DEFAULT_PREPAID_AIR = false;
    private static final Boolean UPDATED_PREPAID_AIR = true;

    private static final Boolean DEFAULT_PREPAID_GROUND = false;
    private static final Boolean UPDATED_PREPAID_GROUND = true;

    private static final Boolean DEFAULT_COD_AIR = false;
    private static final Boolean UPDATED_COD_AIR = true;

    private static final Boolean DEFAULT_COD_GROUND = false;
    private static final Boolean UPDATED_COD_GROUND = true;

    private static final Boolean DEFAULT_REVERSE_AIR = false;
    private static final Boolean UPDATED_REVERSE_AIR = true;

    private static final Boolean DEFAULT_REVERSE_GROUND = false;
    private static final Boolean UPDATED_REVERSE_GROUND = true;

    private static final Boolean DEFAULT_CARD_ON_DELIVERY_AIR = false;
    private static final Boolean UPDATED_CARD_ON_DELIVERY_AIR = true;

    private static final Boolean DEFAULT_CARD_ON_DELIVERY_GROUND = false;
    private static final Boolean UPDATED_CARD_ON_DELIVERY_GROUND = true;

    private static final Boolean DEFAULT_DELIVERY_TYPE_ONE = false;
    private static final Boolean UPDATED_DELIVERY_TYPE_ONE = true;

    private static final Boolean DEFAULT_DELIVERY_TYPE_TWO = false;
    private static final Boolean UPDATED_DELIVERY_TYPE_TWO = true;

    @Autowired
    private PincodeCourierMappingRepository pincodeCourierMappingRepository;


    @Autowired
    private PincodeCourierMappingMapper pincodeCourierMappingMapper;
    

    @Autowired
    private PincodeCourierMappingService pincodeCourierMappingService;

    /**
     * This repository is mocked in the com.hk.logistics.repository.search test package.
     *
     * @see com.hk.logistics.repository.search.PincodeCourierMappingSearchRepositoryMockConfiguration
     */
    @Autowired
    private PincodeCourierMappingSearchRepository mockPincodeCourierMappingSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPincodeCourierMappingMockMvc;

    private PincodeCourierMapping pincodeCourierMapping;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PincodeCourierMappingResource pincodeCourierMappingResource = new PincodeCourierMappingResource(pincodeCourierMappingService);
        this.restPincodeCourierMappingMockMvc = MockMvcBuilders.standaloneSetup(pincodeCourierMappingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PincodeCourierMapping createEntity(EntityManager em) {
        PincodeCourierMapping pincodeCourierMapping = new PincodeCourierMapping()
            .routingCode(DEFAULT_ROUTING_CODE)
            .applicableForCheapestCourier(DEFAULT_APPLICABLE_FOR_CHEAPEST_COURIER)
            .estimatedDeliveryDays(DEFAULT_ESTIMATED_DELIVERY_DAYS)
            .pickupAvailable(DEFAULT_PICKUP_AVAILABLE)
            .prepaidAir(DEFAULT_PREPAID_AIR)
            .prepaidGround(DEFAULT_PREPAID_GROUND)
            .codAir(DEFAULT_COD_AIR)
            .codGround(DEFAULT_COD_GROUND)
            .reverseAir(DEFAULT_REVERSE_AIR)
            .reverseGround(DEFAULT_REVERSE_GROUND)
            .cardOnDeliveryAir(DEFAULT_CARD_ON_DELIVERY_AIR)
            .cardOnDeliveryGround(DEFAULT_CARD_ON_DELIVERY_GROUND)
            .deliveryTypeOne(DEFAULT_DELIVERY_TYPE_ONE)
            .deliveryTypeTwo(DEFAULT_DELIVERY_TYPE_TWO);
        return pincodeCourierMapping;
    }

    @Before
    public void initTest() {
        pincodeCourierMapping = createEntity(em);
    }

    @Test
    @Transactional
    public void createPincodeCourierMapping() throws Exception {
        int databaseSizeBeforeCreate = pincodeCourierMappingRepository.findAll().size();

        // Create the PincodeCourierMapping
        PincodeCourierMappingDTO pincodeCourierMappingDTO = pincodeCourierMappingMapper.toDto(pincodeCourierMapping);
        restPincodeCourierMappingMockMvc.perform(post("/api/pincode-courier-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pincodeCourierMappingDTO)))
            .andExpect(status().isCreated());

        // Validate the PincodeCourierMapping in the database
        List<PincodeCourierMapping> pincodeCourierMappingList = pincodeCourierMappingRepository.findAll();
        assertThat(pincodeCourierMappingList).hasSize(databaseSizeBeforeCreate + 1);
        PincodeCourierMapping testPincodeCourierMapping = pincodeCourierMappingList.get(pincodeCourierMappingList.size() - 1);
        assertThat(testPincodeCourierMapping.getRoutingCode()).isEqualTo(DEFAULT_ROUTING_CODE);
        assertThat(testPincodeCourierMapping.isApplicableForCheapestCourier()).isEqualTo(DEFAULT_APPLICABLE_FOR_CHEAPEST_COURIER);
        assertThat(testPincodeCourierMapping.getEstimatedDeliveryDays()).isEqualTo(DEFAULT_ESTIMATED_DELIVERY_DAYS);
        assertThat(testPincodeCourierMapping.isPickupAvailable()).isEqualTo(DEFAULT_PICKUP_AVAILABLE);
        assertThat(testPincodeCourierMapping.isPrepaidAir()).isEqualTo(DEFAULT_PREPAID_AIR);
        assertThat(testPincodeCourierMapping.isPrepaidGround()).isEqualTo(DEFAULT_PREPAID_GROUND);
        assertThat(testPincodeCourierMapping.isCodAir()).isEqualTo(DEFAULT_COD_AIR);
        assertThat(testPincodeCourierMapping.isCodGround()).isEqualTo(DEFAULT_COD_GROUND);
        assertThat(testPincodeCourierMapping.isReverseAir()).isEqualTo(DEFAULT_REVERSE_AIR);
        assertThat(testPincodeCourierMapping.isReverseGround()).isEqualTo(DEFAULT_REVERSE_GROUND);
        assertThat(testPincodeCourierMapping.isCardOnDeliveryAir()).isEqualTo(DEFAULT_CARD_ON_DELIVERY_AIR);
        assertThat(testPincodeCourierMapping.isCardOnDeliveryGround()).isEqualTo(DEFAULT_CARD_ON_DELIVERY_GROUND);
        assertThat(testPincodeCourierMapping.isDeliveryTypeOne()).isEqualTo(DEFAULT_DELIVERY_TYPE_ONE);
        assertThat(testPincodeCourierMapping.isDeliveryTypeTwo()).isEqualTo(DEFAULT_DELIVERY_TYPE_TWO);

        // Validate the PincodeCourierMapping in Elasticsearch
        verify(mockPincodeCourierMappingSearchRepository, times(1)).save(testPincodeCourierMapping);
    }

    @Test
    @Transactional
    public void createPincodeCourierMappingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pincodeCourierMappingRepository.findAll().size();

        // Create the PincodeCourierMapping with an existing ID
        pincodeCourierMapping.setId(1L);
        PincodeCourierMappingDTO pincodeCourierMappingDTO = pincodeCourierMappingMapper.toDto(pincodeCourierMapping);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPincodeCourierMappingMockMvc.perform(post("/api/pincode-courier-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pincodeCourierMappingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PincodeCourierMapping in the database
        List<PincodeCourierMapping> pincodeCourierMappingList = pincodeCourierMappingRepository.findAll();
        assertThat(pincodeCourierMappingList).hasSize(databaseSizeBeforeCreate);

        // Validate the PincodeCourierMapping in Elasticsearch
        verify(mockPincodeCourierMappingSearchRepository, times(0)).save(pincodeCourierMapping);
    }

    @Test
    @Transactional
    public void checkApplicableForCheapestCourierIsRequired() throws Exception {
        int databaseSizeBeforeTest = pincodeCourierMappingRepository.findAll().size();
        // set the field null
        pincodeCourierMapping.setApplicableForCheapestCourier(null);

        // Create the PincodeCourierMapping, which fails.
        PincodeCourierMappingDTO pincodeCourierMappingDTO = pincodeCourierMappingMapper.toDto(pincodeCourierMapping);

        restPincodeCourierMappingMockMvc.perform(post("/api/pincode-courier-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pincodeCourierMappingDTO)))
            .andExpect(status().isBadRequest());

        List<PincodeCourierMapping> pincodeCourierMappingList = pincodeCourierMappingRepository.findAll();
        assertThat(pincodeCourierMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPickupAvailableIsRequired() throws Exception {
        int databaseSizeBeforeTest = pincodeCourierMappingRepository.findAll().size();
        // set the field null
        pincodeCourierMapping.setPickupAvailable(null);

        // Create the PincodeCourierMapping, which fails.
        PincodeCourierMappingDTO pincodeCourierMappingDTO = pincodeCourierMappingMapper.toDto(pincodeCourierMapping);

        restPincodeCourierMappingMockMvc.perform(post("/api/pincode-courier-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pincodeCourierMappingDTO)))
            .andExpect(status().isBadRequest());

        List<PincodeCourierMapping> pincodeCourierMappingList = pincodeCourierMappingRepository.findAll();
        assertThat(pincodeCourierMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrepaidAirIsRequired() throws Exception {
        int databaseSizeBeforeTest = pincodeCourierMappingRepository.findAll().size();
        // set the field null
        pincodeCourierMapping.setPrepaidAir(null);

        // Create the PincodeCourierMapping, which fails.
        PincodeCourierMappingDTO pincodeCourierMappingDTO = pincodeCourierMappingMapper.toDto(pincodeCourierMapping);

        restPincodeCourierMappingMockMvc.perform(post("/api/pincode-courier-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pincodeCourierMappingDTO)))
            .andExpect(status().isBadRequest());

        List<PincodeCourierMapping> pincodeCourierMappingList = pincodeCourierMappingRepository.findAll();
        assertThat(pincodeCourierMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrepaidGroundIsRequired() throws Exception {
        int databaseSizeBeforeTest = pincodeCourierMappingRepository.findAll().size();
        // set the field null
        pincodeCourierMapping.setPrepaidGround(null);

        // Create the PincodeCourierMapping, which fails.
        PincodeCourierMappingDTO pincodeCourierMappingDTO = pincodeCourierMappingMapper.toDto(pincodeCourierMapping);

        restPincodeCourierMappingMockMvc.perform(post("/api/pincode-courier-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pincodeCourierMappingDTO)))
            .andExpect(status().isBadRequest());

        List<PincodeCourierMapping> pincodeCourierMappingList = pincodeCourierMappingRepository.findAll();
        assertThat(pincodeCourierMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodAirIsRequired() throws Exception {
        int databaseSizeBeforeTest = pincodeCourierMappingRepository.findAll().size();
        // set the field null
        pincodeCourierMapping.setCodAir(null);

        // Create the PincodeCourierMapping, which fails.
        PincodeCourierMappingDTO pincodeCourierMappingDTO = pincodeCourierMappingMapper.toDto(pincodeCourierMapping);

        restPincodeCourierMappingMockMvc.perform(post("/api/pincode-courier-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pincodeCourierMappingDTO)))
            .andExpect(status().isBadRequest());

        List<PincodeCourierMapping> pincodeCourierMappingList = pincodeCourierMappingRepository.findAll();
        assertThat(pincodeCourierMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodGroundIsRequired() throws Exception {
        int databaseSizeBeforeTest = pincodeCourierMappingRepository.findAll().size();
        // set the field null
        pincodeCourierMapping.setCodGround(null);

        // Create the PincodeCourierMapping, which fails.
        PincodeCourierMappingDTO pincodeCourierMappingDTO = pincodeCourierMappingMapper.toDto(pincodeCourierMapping);

        restPincodeCourierMappingMockMvc.perform(post("/api/pincode-courier-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pincodeCourierMappingDTO)))
            .andExpect(status().isBadRequest());

        List<PincodeCourierMapping> pincodeCourierMappingList = pincodeCourierMappingRepository.findAll();
        assertThat(pincodeCourierMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReverseAirIsRequired() throws Exception {
        int databaseSizeBeforeTest = pincodeCourierMappingRepository.findAll().size();
        // set the field null
        pincodeCourierMapping.setReverseAir(null);

        // Create the PincodeCourierMapping, which fails.
        PincodeCourierMappingDTO pincodeCourierMappingDTO = pincodeCourierMappingMapper.toDto(pincodeCourierMapping);

        restPincodeCourierMappingMockMvc.perform(post("/api/pincode-courier-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pincodeCourierMappingDTO)))
            .andExpect(status().isBadRequest());

        List<PincodeCourierMapping> pincodeCourierMappingList = pincodeCourierMappingRepository.findAll();
        assertThat(pincodeCourierMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReverseGroundIsRequired() throws Exception {
        int databaseSizeBeforeTest = pincodeCourierMappingRepository.findAll().size();
        // set the field null
        pincodeCourierMapping.setReverseGround(null);

        // Create the PincodeCourierMapping, which fails.
        PincodeCourierMappingDTO pincodeCourierMappingDTO = pincodeCourierMappingMapper.toDto(pincodeCourierMapping);

        restPincodeCourierMappingMockMvc.perform(post("/api/pincode-courier-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pincodeCourierMappingDTO)))
            .andExpect(status().isBadRequest());

        List<PincodeCourierMapping> pincodeCourierMappingList = pincodeCourierMappingRepository.findAll();
        assertThat(pincodeCourierMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCardOnDeliveryAirIsRequired() throws Exception {
        int databaseSizeBeforeTest = pincodeCourierMappingRepository.findAll().size();
        // set the field null
        pincodeCourierMapping.setCardOnDeliveryAir(null);

        // Create the PincodeCourierMapping, which fails.
        PincodeCourierMappingDTO pincodeCourierMappingDTO = pincodeCourierMappingMapper.toDto(pincodeCourierMapping);

        restPincodeCourierMappingMockMvc.perform(post("/api/pincode-courier-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pincodeCourierMappingDTO)))
            .andExpect(status().isBadRequest());

        List<PincodeCourierMapping> pincodeCourierMappingList = pincodeCourierMappingRepository.findAll();
        assertThat(pincodeCourierMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCardOnDeliveryGroundIsRequired() throws Exception {
        int databaseSizeBeforeTest = pincodeCourierMappingRepository.findAll().size();
        // set the field null
        pincodeCourierMapping.setCardOnDeliveryGround(null);

        // Create the PincodeCourierMapping, which fails.
        PincodeCourierMappingDTO pincodeCourierMappingDTO = pincodeCourierMappingMapper.toDto(pincodeCourierMapping);

        restPincodeCourierMappingMockMvc.perform(post("/api/pincode-courier-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pincodeCourierMappingDTO)))
            .andExpect(status().isBadRequest());

        List<PincodeCourierMapping> pincodeCourierMappingList = pincodeCourierMappingRepository.findAll();
        assertThat(pincodeCourierMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPincodeCourierMappings() throws Exception {
        // Initialize the database
        pincodeCourierMappingRepository.saveAndFlush(pincodeCourierMapping);

        // Get all the pincodeCourierMappingList
        restPincodeCourierMappingMockMvc.perform(get("/api/pincode-courier-mappings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pincodeCourierMapping.getId().intValue())))
            .andExpect(jsonPath("$.[*].routingCode").value(hasItem(DEFAULT_ROUTING_CODE.toString())))
            .andExpect(jsonPath("$.[*].applicableForCheapestCourier").value(hasItem(DEFAULT_APPLICABLE_FOR_CHEAPEST_COURIER.booleanValue())))
            .andExpect(jsonPath("$.[*].estimatedDeliveryDays").value(hasItem(DEFAULT_ESTIMATED_DELIVERY_DAYS.doubleValue())))
            .andExpect(jsonPath("$.[*].pickupAvailable").value(hasItem(DEFAULT_PICKUP_AVAILABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].prepaidAir").value(hasItem(DEFAULT_PREPAID_AIR.booleanValue())))
            .andExpect(jsonPath("$.[*].prepaidGround").value(hasItem(DEFAULT_PREPAID_GROUND.booleanValue())))
            .andExpect(jsonPath("$.[*].codAir").value(hasItem(DEFAULT_COD_AIR.booleanValue())))
            .andExpect(jsonPath("$.[*].codGround").value(hasItem(DEFAULT_COD_GROUND.booleanValue())))
            .andExpect(jsonPath("$.[*].reverseAir").value(hasItem(DEFAULT_REVERSE_AIR.booleanValue())))
            .andExpect(jsonPath("$.[*].reverseGround").value(hasItem(DEFAULT_REVERSE_GROUND.booleanValue())))
            .andExpect(jsonPath("$.[*].cardOnDeliveryAir").value(hasItem(DEFAULT_CARD_ON_DELIVERY_AIR.booleanValue())))
            .andExpect(jsonPath("$.[*].cardOnDeliveryGround").value(hasItem(DEFAULT_CARD_ON_DELIVERY_GROUND.booleanValue())))
            .andExpect(jsonPath("$.[*].deliveryTypeOne").value(hasItem(DEFAULT_DELIVERY_TYPE_ONE.booleanValue())))
            .andExpect(jsonPath("$.[*].deliveryTypeTwo").value(hasItem(DEFAULT_DELIVERY_TYPE_TWO.booleanValue())));
    }
    

    @Test
    @Transactional
    public void getPincodeCourierMapping() throws Exception {
        // Initialize the database
        pincodeCourierMappingRepository.saveAndFlush(pincodeCourierMapping);

        // Get the pincodeCourierMapping
        restPincodeCourierMappingMockMvc.perform(get("/api/pincode-courier-mappings/{id}", pincodeCourierMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pincodeCourierMapping.getId().intValue()))
            .andExpect(jsonPath("$.routingCode").value(DEFAULT_ROUTING_CODE.toString()))
            .andExpect(jsonPath("$.applicableForCheapestCourier").value(DEFAULT_APPLICABLE_FOR_CHEAPEST_COURIER.booleanValue()))
            .andExpect(jsonPath("$.estimatedDeliveryDays").value(DEFAULT_ESTIMATED_DELIVERY_DAYS.doubleValue()))
            .andExpect(jsonPath("$.pickupAvailable").value(DEFAULT_PICKUP_AVAILABLE.booleanValue()))
            .andExpect(jsonPath("$.prepaidAir").value(DEFAULT_PREPAID_AIR.booleanValue()))
            .andExpect(jsonPath("$.prepaidGround").value(DEFAULT_PREPAID_GROUND.booleanValue()))
            .andExpect(jsonPath("$.codAir").value(DEFAULT_COD_AIR.booleanValue()))
            .andExpect(jsonPath("$.codGround").value(DEFAULT_COD_GROUND.booleanValue()))
            .andExpect(jsonPath("$.reverseAir").value(DEFAULT_REVERSE_AIR.booleanValue()))
            .andExpect(jsonPath("$.reverseGround").value(DEFAULT_REVERSE_GROUND.booleanValue()))
            .andExpect(jsonPath("$.cardOnDeliveryAir").value(DEFAULT_CARD_ON_DELIVERY_AIR.booleanValue()))
            .andExpect(jsonPath("$.cardOnDeliveryGround").value(DEFAULT_CARD_ON_DELIVERY_GROUND.booleanValue()))
            .andExpect(jsonPath("$.deliveryTypeOne").value(DEFAULT_DELIVERY_TYPE_ONE.booleanValue()))
            .andExpect(jsonPath("$.deliveryTypeTwo").value(DEFAULT_DELIVERY_TYPE_TWO.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingPincodeCourierMapping() throws Exception {
        // Get the pincodeCourierMapping
        restPincodeCourierMappingMockMvc.perform(get("/api/pincode-courier-mappings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePincodeCourierMapping() throws Exception {
        // Initialize the database
        pincodeCourierMappingRepository.saveAndFlush(pincodeCourierMapping);

        int databaseSizeBeforeUpdate = pincodeCourierMappingRepository.findAll().size();

        // Update the pincodeCourierMapping
        PincodeCourierMapping updatedPincodeCourierMapping = pincodeCourierMappingRepository.findById(pincodeCourierMapping.getId()).get();
        // Disconnect from session so that the updates on updatedPincodeCourierMapping are not directly saved in db
        em.detach(updatedPincodeCourierMapping);
        updatedPincodeCourierMapping
            .routingCode(UPDATED_ROUTING_CODE)
            .applicableForCheapestCourier(UPDATED_APPLICABLE_FOR_CHEAPEST_COURIER)
            .estimatedDeliveryDays(UPDATED_ESTIMATED_DELIVERY_DAYS)
            .pickupAvailable(UPDATED_PICKUP_AVAILABLE)
            .prepaidAir(UPDATED_PREPAID_AIR)
            .prepaidGround(UPDATED_PREPAID_GROUND)
            .codAir(UPDATED_COD_AIR)
            .codGround(UPDATED_COD_GROUND)
            .reverseAir(UPDATED_REVERSE_AIR)
            .reverseGround(UPDATED_REVERSE_GROUND)
            .cardOnDeliveryAir(UPDATED_CARD_ON_DELIVERY_AIR)
            .cardOnDeliveryGround(UPDATED_CARD_ON_DELIVERY_GROUND)
            .deliveryTypeOne(UPDATED_DELIVERY_TYPE_ONE)
            .deliveryTypeTwo(UPDATED_DELIVERY_TYPE_TWO);
        PincodeCourierMappingDTO pincodeCourierMappingDTO = pincodeCourierMappingMapper.toDto(updatedPincodeCourierMapping);

        restPincodeCourierMappingMockMvc.perform(put("/api/pincode-courier-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pincodeCourierMappingDTO)))
            .andExpect(status().isOk());

        // Validate the PincodeCourierMapping in the database
        List<PincodeCourierMapping> pincodeCourierMappingList = pincodeCourierMappingRepository.findAll();
        assertThat(pincodeCourierMappingList).hasSize(databaseSizeBeforeUpdate);
        PincodeCourierMapping testPincodeCourierMapping = pincodeCourierMappingList.get(pincodeCourierMappingList.size() - 1);
        assertThat(testPincodeCourierMapping.getRoutingCode()).isEqualTo(UPDATED_ROUTING_CODE);
        assertThat(testPincodeCourierMapping.isApplicableForCheapestCourier()).isEqualTo(UPDATED_APPLICABLE_FOR_CHEAPEST_COURIER);
        assertThat(testPincodeCourierMapping.getEstimatedDeliveryDays()).isEqualTo(UPDATED_ESTIMATED_DELIVERY_DAYS);
        assertThat(testPincodeCourierMapping.isPickupAvailable()).isEqualTo(UPDATED_PICKUP_AVAILABLE);
        assertThat(testPincodeCourierMapping.isPrepaidAir()).isEqualTo(UPDATED_PREPAID_AIR);
        assertThat(testPincodeCourierMapping.isPrepaidGround()).isEqualTo(UPDATED_PREPAID_GROUND);
        assertThat(testPincodeCourierMapping.isCodAir()).isEqualTo(UPDATED_COD_AIR);
        assertThat(testPincodeCourierMapping.isCodGround()).isEqualTo(UPDATED_COD_GROUND);
        assertThat(testPincodeCourierMapping.isReverseAir()).isEqualTo(UPDATED_REVERSE_AIR);
        assertThat(testPincodeCourierMapping.isReverseGround()).isEqualTo(UPDATED_REVERSE_GROUND);
        assertThat(testPincodeCourierMapping.isCardOnDeliveryAir()).isEqualTo(UPDATED_CARD_ON_DELIVERY_AIR);
        assertThat(testPincodeCourierMapping.isCardOnDeliveryGround()).isEqualTo(UPDATED_CARD_ON_DELIVERY_GROUND);
        assertThat(testPincodeCourierMapping.isDeliveryTypeOne()).isEqualTo(UPDATED_DELIVERY_TYPE_ONE);
        assertThat(testPincodeCourierMapping.isDeliveryTypeTwo()).isEqualTo(UPDATED_DELIVERY_TYPE_TWO);

        // Validate the PincodeCourierMapping in Elasticsearch
        verify(mockPincodeCourierMappingSearchRepository, times(1)).save(testPincodeCourierMapping);
    }

    @Test
    @Transactional
    public void updateNonExistingPincodeCourierMapping() throws Exception {
        int databaseSizeBeforeUpdate = pincodeCourierMappingRepository.findAll().size();

        // Create the PincodeCourierMapping
        PincodeCourierMappingDTO pincodeCourierMappingDTO = pincodeCourierMappingMapper.toDto(pincodeCourierMapping);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPincodeCourierMappingMockMvc.perform(put("/api/pincode-courier-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pincodeCourierMappingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PincodeCourierMapping in the database
        List<PincodeCourierMapping> pincodeCourierMappingList = pincodeCourierMappingRepository.findAll();
        assertThat(pincodeCourierMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PincodeCourierMapping in Elasticsearch
        verify(mockPincodeCourierMappingSearchRepository, times(0)).save(pincodeCourierMapping);
    }

    @Test
    @Transactional
    public void deletePincodeCourierMapping() throws Exception {
        // Initialize the database
        pincodeCourierMappingRepository.saveAndFlush(pincodeCourierMapping);

        int databaseSizeBeforeDelete = pincodeCourierMappingRepository.findAll().size();

        // Get the pincodeCourierMapping
        restPincodeCourierMappingMockMvc.perform(delete("/api/pincode-courier-mappings/{id}", pincodeCourierMapping.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PincodeCourierMapping> pincodeCourierMappingList = pincodeCourierMappingRepository.findAll();
        assertThat(pincodeCourierMappingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PincodeCourierMapping in Elasticsearch
        verify(mockPincodeCourierMappingSearchRepository, times(1)).deleteById(pincodeCourierMapping.getId());
    }

    @Test
    @Transactional
    public void searchPincodeCourierMapping() throws Exception {
        // Initialize the database
        pincodeCourierMappingRepository.saveAndFlush(pincodeCourierMapping);
        when(mockPincodeCourierMappingSearchRepository.search(queryStringQuery("id:" + pincodeCourierMapping.getId())))
            .thenReturn(Collections.singletonList(pincodeCourierMapping));
        // Search the pincodeCourierMapping
        restPincodeCourierMappingMockMvc.perform(get("/api/_search/pincode-courier-mappings?query=id:" + pincodeCourierMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pincodeCourierMapping.getId().intValue())))
            .andExpect(jsonPath("$.[*].routingCode").value(hasItem(DEFAULT_ROUTING_CODE.toString())))
            .andExpect(jsonPath("$.[*].applicableForCheapestCourier").value(hasItem(DEFAULT_APPLICABLE_FOR_CHEAPEST_COURIER.booleanValue())))
            .andExpect(jsonPath("$.[*].estimatedDeliveryDays").value(hasItem(DEFAULT_ESTIMATED_DELIVERY_DAYS.doubleValue())))
            .andExpect(jsonPath("$.[*].pickupAvailable").value(hasItem(DEFAULT_PICKUP_AVAILABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].prepaidAir").value(hasItem(DEFAULT_PREPAID_AIR.booleanValue())))
            .andExpect(jsonPath("$.[*].prepaidGround").value(hasItem(DEFAULT_PREPAID_GROUND.booleanValue())))
            .andExpect(jsonPath("$.[*].codAir").value(hasItem(DEFAULT_COD_AIR.booleanValue())))
            .andExpect(jsonPath("$.[*].codGround").value(hasItem(DEFAULT_COD_GROUND.booleanValue())))
            .andExpect(jsonPath("$.[*].reverseAir").value(hasItem(DEFAULT_REVERSE_AIR.booleanValue())))
            .andExpect(jsonPath("$.[*].reverseGround").value(hasItem(DEFAULT_REVERSE_GROUND.booleanValue())))
            .andExpect(jsonPath("$.[*].cardOnDeliveryAir").value(hasItem(DEFAULT_CARD_ON_DELIVERY_AIR.booleanValue())))
            .andExpect(jsonPath("$.[*].cardOnDeliveryGround").value(hasItem(DEFAULT_CARD_ON_DELIVERY_GROUND.booleanValue())))
            .andExpect(jsonPath("$.[*].deliveryTypeOne").value(hasItem(DEFAULT_DELIVERY_TYPE_ONE.booleanValue())))
            .andExpect(jsonPath("$.[*].deliveryTypeTwo").value(hasItem(DEFAULT_DELIVERY_TYPE_TWO.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PincodeCourierMapping.class);
        PincodeCourierMapping pincodeCourierMapping1 = new PincodeCourierMapping();
        pincodeCourierMapping1.setId(1L);
        PincodeCourierMapping pincodeCourierMapping2 = new PincodeCourierMapping();
        pincodeCourierMapping2.setId(pincodeCourierMapping1.getId());
        assertThat(pincodeCourierMapping1).isEqualTo(pincodeCourierMapping2);
        pincodeCourierMapping2.setId(2L);
        assertThat(pincodeCourierMapping1).isNotEqualTo(pincodeCourierMapping2);
        pincodeCourierMapping1.setId(null);
        assertThat(pincodeCourierMapping1).isNotEqualTo(pincodeCourierMapping2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PincodeCourierMappingDTO.class);
        PincodeCourierMappingDTO pincodeCourierMappingDTO1 = new PincodeCourierMappingDTO();
        pincodeCourierMappingDTO1.setId(1L);
        PincodeCourierMappingDTO pincodeCourierMappingDTO2 = new PincodeCourierMappingDTO();
        assertThat(pincodeCourierMappingDTO1).isNotEqualTo(pincodeCourierMappingDTO2);
        pincodeCourierMappingDTO2.setId(pincodeCourierMappingDTO1.getId());
        assertThat(pincodeCourierMappingDTO1).isEqualTo(pincodeCourierMappingDTO2);
        pincodeCourierMappingDTO2.setId(2L);
        assertThat(pincodeCourierMappingDTO1).isNotEqualTo(pincodeCourierMappingDTO2);
        pincodeCourierMappingDTO1.setId(null);
        assertThat(pincodeCourierMappingDTO1).isNotEqualTo(pincodeCourierMappingDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pincodeCourierMappingMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pincodeCourierMappingMapper.fromId(null)).isNull();
    }
}
