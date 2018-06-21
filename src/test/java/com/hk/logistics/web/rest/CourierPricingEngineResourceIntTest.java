package com.hk.logistics.web.rest;

import com.hk.logistics.HkLogisticsApp;

import com.hk.logistics.domain.CourierPricingEngine;
import com.hk.logistics.repository.CourierPricingEngineRepository;
import com.hk.logistics.repository.search.CourierPricingEngineSearchRepository;
import com.hk.logistics.service.CourierPricingEngineService;
import com.hk.logistics.service.dto.CourierPricingEngineDTO;
import com.hk.logistics.service.mapper.CourierPricingEngineMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Test class for the CourierPricingEngineResource REST controller.
 *
 * @see CourierPricingEngineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HkLogisticsApp.class)
public class CourierPricingEngineResourceIntTest {

    private static final Double DEFAULT_FIRST_BASE_WT = 1D;
    private static final Double UPDATED_FIRST_BASE_WT = 2D;

    private static final Double DEFAULT_FIRST_BASE_COST = 1D;
    private static final Double UPDATED_FIRST_BASE_COST = 2D;

    private static final Double DEFAULT_SECOND_BASE_WT = 1D;
    private static final Double UPDATED_SECOND_BASE_WT = 2D;

    private static final Double DEFAULT_SECOND_BASE_COST = 1D;
    private static final Double UPDATED_SECOND_BASE_COST = 2D;

    private static final Double DEFAULT_ADDITIONAL_WT = 1D;
    private static final Double UPDATED_ADDITIONAL_WT = 2D;

    private static final Double DEFAULT_ADDITIONAL_COST = 1D;
    private static final Double UPDATED_ADDITIONAL_COST = 2D;

    private static final Double DEFAULT_FUEL_SURCHARGE = 1D;
    private static final Double UPDATED_FUEL_SURCHARGE = 2D;

    private static final Double DEFAULT_MIN_COD_CHARGES = 1D;
    private static final Double UPDATED_MIN_COD_CHARGES = 2D;

    private static final Double DEFAULT_COD_CUTOFF_AMOUNT = 1D;
    private static final Double UPDATED_COD_CUTOFF_AMOUNT = 2D;

    private static final Double DEFAULT_VARIABLE_COD_CHARGES = 1D;
    private static final Double UPDATED_VARIABLE_COD_CHARGES = 2D;

    private static final LocalDate DEFAULT_VALID_UPTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VALID_UPTO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_COST_PARAMETERS = "AAAAAAAAAA";
    private static final String UPDATED_COST_PARAMETERS = "BBBBBBBBBB";

    @Autowired
    private CourierPricingEngineRepository courierPricingEngineRepository;


    @Autowired
    private CourierPricingEngineMapper courierPricingEngineMapper;
    

    @Autowired
    private CourierPricingEngineService courierPricingEngineService;

    /**
     * This repository is mocked in the com.hk.logistics.repository.search test package.
     *
     * @see com.hk.logistics.repository.search.CourierPricingEngineSearchRepositoryMockConfiguration
     */
    @Autowired
    private CourierPricingEngineSearchRepository mockCourierPricingEngineSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCourierPricingEngineMockMvc;

    private CourierPricingEngine courierPricingEngine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CourierPricingEngineResource courierPricingEngineResource = new CourierPricingEngineResource(courierPricingEngineService);
        this.restCourierPricingEngineMockMvc = MockMvcBuilders.standaloneSetup(courierPricingEngineResource)
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
    public static CourierPricingEngine createEntity(EntityManager em) {
        CourierPricingEngine courierPricingEngine = new CourierPricingEngine()
            .firstBaseWt(DEFAULT_FIRST_BASE_WT)
            .firstBaseCost(DEFAULT_FIRST_BASE_COST)
            .secondBaseWt(DEFAULT_SECOND_BASE_WT)
            .secondBaseCost(DEFAULT_SECOND_BASE_COST)
            .additionalWt(DEFAULT_ADDITIONAL_WT)
            .additionalCost(DEFAULT_ADDITIONAL_COST)
            .fuelSurcharge(DEFAULT_FUEL_SURCHARGE)
            .minCodCharges(DEFAULT_MIN_COD_CHARGES)
            .codCutoffAmount(DEFAULT_COD_CUTOFF_AMOUNT)
            .variableCodCharges(DEFAULT_VARIABLE_COD_CHARGES)
            .validUpto(DEFAULT_VALID_UPTO)
            .costParameters(DEFAULT_COST_PARAMETERS);
        return courierPricingEngine;
    }

    @Before
    public void initTest() {
        courierPricingEngine = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourierPricingEngine() throws Exception {
        int databaseSizeBeforeCreate = courierPricingEngineRepository.findAll().size();

        // Create the CourierPricingEngine
        CourierPricingEngineDTO courierPricingEngineDTO = courierPricingEngineMapper.toDto(courierPricingEngine);
        restCourierPricingEngineMockMvc.perform(post("/api/courier-pricing-engines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courierPricingEngineDTO)))
            .andExpect(status().isCreated());

        // Validate the CourierPricingEngine in the database
        List<CourierPricingEngine> courierPricingEngineList = courierPricingEngineRepository.findAll();
        assertThat(courierPricingEngineList).hasSize(databaseSizeBeforeCreate + 1);
        CourierPricingEngine testCourierPricingEngine = courierPricingEngineList.get(courierPricingEngineList.size() - 1);
        assertThat(testCourierPricingEngine.getFirstBaseWt()).isEqualTo(DEFAULT_FIRST_BASE_WT);
        assertThat(testCourierPricingEngine.getFirstBaseCost()).isEqualTo(DEFAULT_FIRST_BASE_COST);
        assertThat(testCourierPricingEngine.getSecondBaseWt()).isEqualTo(DEFAULT_SECOND_BASE_WT);
        assertThat(testCourierPricingEngine.getSecondBaseCost()).isEqualTo(DEFAULT_SECOND_BASE_COST);
        assertThat(testCourierPricingEngine.getAdditionalWt()).isEqualTo(DEFAULT_ADDITIONAL_WT);
        assertThat(testCourierPricingEngine.getAdditionalCost()).isEqualTo(DEFAULT_ADDITIONAL_COST);
        assertThat(testCourierPricingEngine.getFuelSurcharge()).isEqualTo(DEFAULT_FUEL_SURCHARGE);
        assertThat(testCourierPricingEngine.getMinCodCharges()).isEqualTo(DEFAULT_MIN_COD_CHARGES);
        assertThat(testCourierPricingEngine.getCodCutoffAmount()).isEqualTo(DEFAULT_COD_CUTOFF_AMOUNT);
        assertThat(testCourierPricingEngine.getVariableCodCharges()).isEqualTo(DEFAULT_VARIABLE_COD_CHARGES);
        assertThat(testCourierPricingEngine.getValidUpto()).isEqualTo(DEFAULT_VALID_UPTO);
        assertThat(testCourierPricingEngine.getCostParameters()).isEqualTo(DEFAULT_COST_PARAMETERS);

        // Validate the CourierPricingEngine in Elasticsearch
        verify(mockCourierPricingEngineSearchRepository, times(1)).save(testCourierPricingEngine);
    }

    @Test
    @Transactional
    public void createCourierPricingEngineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courierPricingEngineRepository.findAll().size();

        // Create the CourierPricingEngine with an existing ID
        courierPricingEngine.setId(1L);
        CourierPricingEngineDTO courierPricingEngineDTO = courierPricingEngineMapper.toDto(courierPricingEngine);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourierPricingEngineMockMvc.perform(post("/api/courier-pricing-engines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courierPricingEngineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CourierPricingEngine in the database
        List<CourierPricingEngine> courierPricingEngineList = courierPricingEngineRepository.findAll();
        assertThat(courierPricingEngineList).hasSize(databaseSizeBeforeCreate);

        // Validate the CourierPricingEngine in Elasticsearch
        verify(mockCourierPricingEngineSearchRepository, times(0)).save(courierPricingEngine);
    }

    @Test
    @Transactional
    public void checkFirstBaseWtIsRequired() throws Exception {
        int databaseSizeBeforeTest = courierPricingEngineRepository.findAll().size();
        // set the field null
        courierPricingEngine.setFirstBaseWt(null);

        // Create the CourierPricingEngine, which fails.
        CourierPricingEngineDTO courierPricingEngineDTO = courierPricingEngineMapper.toDto(courierPricingEngine);

        restCourierPricingEngineMockMvc.perform(post("/api/courier-pricing-engines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courierPricingEngineDTO)))
            .andExpect(status().isBadRequest());

        List<CourierPricingEngine> courierPricingEngineList = courierPricingEngineRepository.findAll();
        assertThat(courierPricingEngineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFirstBaseCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = courierPricingEngineRepository.findAll().size();
        // set the field null
        courierPricingEngine.setFirstBaseCost(null);

        // Create the CourierPricingEngine, which fails.
        CourierPricingEngineDTO courierPricingEngineDTO = courierPricingEngineMapper.toDto(courierPricingEngine);

        restCourierPricingEngineMockMvc.perform(post("/api/courier-pricing-engines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courierPricingEngineDTO)))
            .andExpect(status().isBadRequest());

        List<CourierPricingEngine> courierPricingEngineList = courierPricingEngineRepository.findAll();
        assertThat(courierPricingEngineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdditionalWtIsRequired() throws Exception {
        int databaseSizeBeforeTest = courierPricingEngineRepository.findAll().size();
        // set the field null
        courierPricingEngine.setAdditionalWt(null);

        // Create the CourierPricingEngine, which fails.
        CourierPricingEngineDTO courierPricingEngineDTO = courierPricingEngineMapper.toDto(courierPricingEngine);

        restCourierPricingEngineMockMvc.perform(post("/api/courier-pricing-engines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courierPricingEngineDTO)))
            .andExpect(status().isBadRequest());

        List<CourierPricingEngine> courierPricingEngineList = courierPricingEngineRepository.findAll();
        assertThat(courierPricingEngineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdditionalCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = courierPricingEngineRepository.findAll().size();
        // set the field null
        courierPricingEngine.setAdditionalCost(null);

        // Create the CourierPricingEngine, which fails.
        CourierPricingEngineDTO courierPricingEngineDTO = courierPricingEngineMapper.toDto(courierPricingEngine);

        restCourierPricingEngineMockMvc.perform(post("/api/courier-pricing-engines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courierPricingEngineDTO)))
            .andExpect(status().isBadRequest());

        List<CourierPricingEngine> courierPricingEngineList = courierPricingEngineRepository.findAll();
        assertThat(courierPricingEngineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCourierPricingEngines() throws Exception {
        // Initialize the database
        courierPricingEngineRepository.saveAndFlush(courierPricingEngine);

        // Get all the courierPricingEngineList
        restCourierPricingEngineMockMvc.perform(get("/api/courier-pricing-engines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courierPricingEngine.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstBaseWt").value(hasItem(DEFAULT_FIRST_BASE_WT.doubleValue())))
            .andExpect(jsonPath("$.[*].firstBaseCost").value(hasItem(DEFAULT_FIRST_BASE_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].secondBaseWt").value(hasItem(DEFAULT_SECOND_BASE_WT.doubleValue())))
            .andExpect(jsonPath("$.[*].secondBaseCost").value(hasItem(DEFAULT_SECOND_BASE_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].additionalWt").value(hasItem(DEFAULT_ADDITIONAL_WT.doubleValue())))
            .andExpect(jsonPath("$.[*].additionalCost").value(hasItem(DEFAULT_ADDITIONAL_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].fuelSurcharge").value(hasItem(DEFAULT_FUEL_SURCHARGE.doubleValue())))
            .andExpect(jsonPath("$.[*].minCodCharges").value(hasItem(DEFAULT_MIN_COD_CHARGES.doubleValue())))
            .andExpect(jsonPath("$.[*].codCutoffAmount").value(hasItem(DEFAULT_COD_CUTOFF_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].variableCodCharges").value(hasItem(DEFAULT_VARIABLE_COD_CHARGES.doubleValue())))
            .andExpect(jsonPath("$.[*].validUpto").value(hasItem(DEFAULT_VALID_UPTO.toString())))
            .andExpect(jsonPath("$.[*].costParameters").value(hasItem(DEFAULT_COST_PARAMETERS.toString())));
    }
    

    @Test
    @Transactional
    public void getCourierPricingEngine() throws Exception {
        // Initialize the database
        courierPricingEngineRepository.saveAndFlush(courierPricingEngine);

        // Get the courierPricingEngine
        restCourierPricingEngineMockMvc.perform(get("/api/courier-pricing-engines/{id}", courierPricingEngine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(courierPricingEngine.getId().intValue()))
            .andExpect(jsonPath("$.firstBaseWt").value(DEFAULT_FIRST_BASE_WT.doubleValue()))
            .andExpect(jsonPath("$.firstBaseCost").value(DEFAULT_FIRST_BASE_COST.doubleValue()))
            .andExpect(jsonPath("$.secondBaseWt").value(DEFAULT_SECOND_BASE_WT.doubleValue()))
            .andExpect(jsonPath("$.secondBaseCost").value(DEFAULT_SECOND_BASE_COST.doubleValue()))
            .andExpect(jsonPath("$.additionalWt").value(DEFAULT_ADDITIONAL_WT.doubleValue()))
            .andExpect(jsonPath("$.additionalCost").value(DEFAULT_ADDITIONAL_COST.doubleValue()))
            .andExpect(jsonPath("$.fuelSurcharge").value(DEFAULT_FUEL_SURCHARGE.doubleValue()))
            .andExpect(jsonPath("$.minCodCharges").value(DEFAULT_MIN_COD_CHARGES.doubleValue()))
            .andExpect(jsonPath("$.codCutoffAmount").value(DEFAULT_COD_CUTOFF_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.variableCodCharges").value(DEFAULT_VARIABLE_COD_CHARGES.doubleValue()))
            .andExpect(jsonPath("$.validUpto").value(DEFAULT_VALID_UPTO.toString()))
            .andExpect(jsonPath("$.costParameters").value(DEFAULT_COST_PARAMETERS.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingCourierPricingEngine() throws Exception {
        // Get the courierPricingEngine
        restCourierPricingEngineMockMvc.perform(get("/api/courier-pricing-engines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourierPricingEngine() throws Exception {
        // Initialize the database
        courierPricingEngineRepository.saveAndFlush(courierPricingEngine);

        int databaseSizeBeforeUpdate = courierPricingEngineRepository.findAll().size();

        // Update the courierPricingEngine
        CourierPricingEngine updatedCourierPricingEngine = courierPricingEngineRepository.findById(courierPricingEngine.getId()).get();
        // Disconnect from session so that the updates on updatedCourierPricingEngine are not directly saved in db
        em.detach(updatedCourierPricingEngine);
        updatedCourierPricingEngine
            .firstBaseWt(UPDATED_FIRST_BASE_WT)
            .firstBaseCost(UPDATED_FIRST_BASE_COST)
            .secondBaseWt(UPDATED_SECOND_BASE_WT)
            .secondBaseCost(UPDATED_SECOND_BASE_COST)
            .additionalWt(UPDATED_ADDITIONAL_WT)
            .additionalCost(UPDATED_ADDITIONAL_COST)
            .fuelSurcharge(UPDATED_FUEL_SURCHARGE)
            .minCodCharges(UPDATED_MIN_COD_CHARGES)
            .codCutoffAmount(UPDATED_COD_CUTOFF_AMOUNT)
            .variableCodCharges(UPDATED_VARIABLE_COD_CHARGES)
            .validUpto(UPDATED_VALID_UPTO)
            .costParameters(UPDATED_COST_PARAMETERS);
        CourierPricingEngineDTO courierPricingEngineDTO = courierPricingEngineMapper.toDto(updatedCourierPricingEngine);

        restCourierPricingEngineMockMvc.perform(put("/api/courier-pricing-engines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courierPricingEngineDTO)))
            .andExpect(status().isOk());

        // Validate the CourierPricingEngine in the database
        List<CourierPricingEngine> courierPricingEngineList = courierPricingEngineRepository.findAll();
        assertThat(courierPricingEngineList).hasSize(databaseSizeBeforeUpdate);
        CourierPricingEngine testCourierPricingEngine = courierPricingEngineList.get(courierPricingEngineList.size() - 1);
        assertThat(testCourierPricingEngine.getFirstBaseWt()).isEqualTo(UPDATED_FIRST_BASE_WT);
        assertThat(testCourierPricingEngine.getFirstBaseCost()).isEqualTo(UPDATED_FIRST_BASE_COST);
        assertThat(testCourierPricingEngine.getSecondBaseWt()).isEqualTo(UPDATED_SECOND_BASE_WT);
        assertThat(testCourierPricingEngine.getSecondBaseCost()).isEqualTo(UPDATED_SECOND_BASE_COST);
        assertThat(testCourierPricingEngine.getAdditionalWt()).isEqualTo(UPDATED_ADDITIONAL_WT);
        assertThat(testCourierPricingEngine.getAdditionalCost()).isEqualTo(UPDATED_ADDITIONAL_COST);
        assertThat(testCourierPricingEngine.getFuelSurcharge()).isEqualTo(UPDATED_FUEL_SURCHARGE);
        assertThat(testCourierPricingEngine.getMinCodCharges()).isEqualTo(UPDATED_MIN_COD_CHARGES);
        assertThat(testCourierPricingEngine.getCodCutoffAmount()).isEqualTo(UPDATED_COD_CUTOFF_AMOUNT);
        assertThat(testCourierPricingEngine.getVariableCodCharges()).isEqualTo(UPDATED_VARIABLE_COD_CHARGES);
        assertThat(testCourierPricingEngine.getValidUpto()).isEqualTo(UPDATED_VALID_UPTO);
        assertThat(testCourierPricingEngine.getCostParameters()).isEqualTo(UPDATED_COST_PARAMETERS);

        // Validate the CourierPricingEngine in Elasticsearch
        verify(mockCourierPricingEngineSearchRepository, times(1)).save(testCourierPricingEngine);
    }

    @Test
    @Transactional
    public void updateNonExistingCourierPricingEngine() throws Exception {
        int databaseSizeBeforeUpdate = courierPricingEngineRepository.findAll().size();

        // Create the CourierPricingEngine
        CourierPricingEngineDTO courierPricingEngineDTO = courierPricingEngineMapper.toDto(courierPricingEngine);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCourierPricingEngineMockMvc.perform(put("/api/courier-pricing-engines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courierPricingEngineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CourierPricingEngine in the database
        List<CourierPricingEngine> courierPricingEngineList = courierPricingEngineRepository.findAll();
        assertThat(courierPricingEngineList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CourierPricingEngine in Elasticsearch
        verify(mockCourierPricingEngineSearchRepository, times(0)).save(courierPricingEngine);
    }

    @Test
    @Transactional
    public void deleteCourierPricingEngine() throws Exception {
        // Initialize the database
        courierPricingEngineRepository.saveAndFlush(courierPricingEngine);

        int databaseSizeBeforeDelete = courierPricingEngineRepository.findAll().size();

        // Get the courierPricingEngine
        restCourierPricingEngineMockMvc.perform(delete("/api/courier-pricing-engines/{id}", courierPricingEngine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CourierPricingEngine> courierPricingEngineList = courierPricingEngineRepository.findAll();
        assertThat(courierPricingEngineList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CourierPricingEngine in Elasticsearch
        verify(mockCourierPricingEngineSearchRepository, times(1)).deleteById(courierPricingEngine.getId());
    }

    @Test
    @Transactional
    public void searchCourierPricingEngine() throws Exception {
        // Initialize the database
        courierPricingEngineRepository.saveAndFlush(courierPricingEngine);
        when(mockCourierPricingEngineSearchRepository.search(queryStringQuery("id:" + courierPricingEngine.getId())))
            .thenReturn(Collections.singletonList(courierPricingEngine));
        // Search the courierPricingEngine
        restCourierPricingEngineMockMvc.perform(get("/api/_search/courier-pricing-engines?query=id:" + courierPricingEngine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courierPricingEngine.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstBaseWt").value(hasItem(DEFAULT_FIRST_BASE_WT.doubleValue())))
            .andExpect(jsonPath("$.[*].firstBaseCost").value(hasItem(DEFAULT_FIRST_BASE_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].secondBaseWt").value(hasItem(DEFAULT_SECOND_BASE_WT.doubleValue())))
            .andExpect(jsonPath("$.[*].secondBaseCost").value(hasItem(DEFAULT_SECOND_BASE_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].additionalWt").value(hasItem(DEFAULT_ADDITIONAL_WT.doubleValue())))
            .andExpect(jsonPath("$.[*].additionalCost").value(hasItem(DEFAULT_ADDITIONAL_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].fuelSurcharge").value(hasItem(DEFAULT_FUEL_SURCHARGE.doubleValue())))
            .andExpect(jsonPath("$.[*].minCodCharges").value(hasItem(DEFAULT_MIN_COD_CHARGES.doubleValue())))
            .andExpect(jsonPath("$.[*].codCutoffAmount").value(hasItem(DEFAULT_COD_CUTOFF_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].variableCodCharges").value(hasItem(DEFAULT_VARIABLE_COD_CHARGES.doubleValue())))
            .andExpect(jsonPath("$.[*].validUpto").value(hasItem(DEFAULT_VALID_UPTO.toString())))
            .andExpect(jsonPath("$.[*].costParameters").value(hasItem(DEFAULT_COST_PARAMETERS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourierPricingEngine.class);
        CourierPricingEngine courierPricingEngine1 = new CourierPricingEngine();
        courierPricingEngine1.setId(1L);
        CourierPricingEngine courierPricingEngine2 = new CourierPricingEngine();
        courierPricingEngine2.setId(courierPricingEngine1.getId());
        assertThat(courierPricingEngine1).isEqualTo(courierPricingEngine2);
        courierPricingEngine2.setId(2L);
        assertThat(courierPricingEngine1).isNotEqualTo(courierPricingEngine2);
        courierPricingEngine1.setId(null);
        assertThat(courierPricingEngine1).isNotEqualTo(courierPricingEngine2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourierPricingEngineDTO.class);
        CourierPricingEngineDTO courierPricingEngineDTO1 = new CourierPricingEngineDTO();
        courierPricingEngineDTO1.setId(1L);
        CourierPricingEngineDTO courierPricingEngineDTO2 = new CourierPricingEngineDTO();
        assertThat(courierPricingEngineDTO1).isNotEqualTo(courierPricingEngineDTO2);
        courierPricingEngineDTO2.setId(courierPricingEngineDTO1.getId());
        assertThat(courierPricingEngineDTO1).isEqualTo(courierPricingEngineDTO2);
        courierPricingEngineDTO2.setId(2L);
        assertThat(courierPricingEngineDTO1).isNotEqualTo(courierPricingEngineDTO2);
        courierPricingEngineDTO1.setId(null);
        assertThat(courierPricingEngineDTO1).isNotEqualTo(courierPricingEngineDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(courierPricingEngineMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(courierPricingEngineMapper.fromId(null)).isNull();
    }
}
