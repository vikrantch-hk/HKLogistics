package com.hk.logistics.web.rest;

import com.hk.logistics.HkLogisticsApp;

import com.hk.logistics.domain.Awb;
import com.hk.logistics.repository.AwbRepository;
import com.hk.logistics.repository.search.AwbSearchRepository;
import com.hk.logistics.service.AwbService;
import com.hk.logistics.service.dto.AwbDTO;
import com.hk.logistics.service.mapper.AwbMapper;
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
 * Test class for the AwbResource REST controller.
 *
 * @see AwbResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HkLogisticsApp.class)
public class AwbResourceIntTest {

    private static final String DEFAULT_AWB_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_AWB_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_AWB_BAR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_AWB_BAR_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_COD = false;
    private static final Boolean UPDATED_COD = true;

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_RETURN_AWB_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_RETURN_AWB_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_RETURN_AWB_BAR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_RETURN_AWB_BAR_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_BRIGHT_AWB = false;
    private static final Boolean UPDATED_IS_BRIGHT_AWB = true;

    @Autowired
    private AwbRepository awbRepository;


    @Autowired
    private AwbMapper awbMapper;
    

    @Autowired
    private AwbService awbService;

    /**
     * This repository is mocked in the com.hk.logistics.repository.search test package.
     *
     * @see com.hk.logistics.repository.search.AwbSearchRepositoryMockConfiguration
     */
    @Autowired
    private AwbSearchRepository mockAwbSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAwbMockMvc;

    private Awb awb;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AwbResource awbResource = new AwbResource(awbService);
        this.restAwbMockMvc = MockMvcBuilders.standaloneSetup(awbResource)
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
    public static Awb createEntity(EntityManager em) {
        Awb awb = new Awb()
            .awbNumber(DEFAULT_AWB_NUMBER)
            .awbBarCode(DEFAULT_AWB_BAR_CODE)
            .cod(DEFAULT_COD)
            .createDate(DEFAULT_CREATE_DATE)
            .returnAwbNumber(DEFAULT_RETURN_AWB_NUMBER)
            .returnAwbBarCode(DEFAULT_RETURN_AWB_BAR_CODE)
            .isBrightAwb(DEFAULT_IS_BRIGHT_AWB);
        return awb;
    }

    @Before
    public void initTest() {
        awb = createEntity(em);
    }

    @Test
    @Transactional
    public void createAwb() throws Exception {
        int databaseSizeBeforeCreate = awbRepository.findAll().size();

        // Create the Awb
        AwbDTO awbDTO = awbMapper.toDto(awb);
        restAwbMockMvc.perform(post("/api/awbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(awbDTO)))
            .andExpect(status().isCreated());

        // Validate the Awb in the database
        List<Awb> awbList = awbRepository.findAll();
        assertThat(awbList).hasSize(databaseSizeBeforeCreate + 1);
        Awb testAwb = awbList.get(awbList.size() - 1);
        assertThat(testAwb.getAwbNumber()).isEqualTo(DEFAULT_AWB_NUMBER);
        assertThat(testAwb.getAwbBarCode()).isEqualTo(DEFAULT_AWB_BAR_CODE);
        assertThat(testAwb.isCod()).isEqualTo(DEFAULT_COD);
        assertThat(testAwb.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testAwb.getReturnAwbNumber()).isEqualTo(DEFAULT_RETURN_AWB_NUMBER);
        assertThat(testAwb.getReturnAwbBarCode()).isEqualTo(DEFAULT_RETURN_AWB_BAR_CODE);
        assertThat(testAwb.isIsBrightAwb()).isEqualTo(DEFAULT_IS_BRIGHT_AWB);

        // Validate the Awb in Elasticsearch
        verify(mockAwbSearchRepository, times(1)).save(testAwb);
    }

    @Test
    @Transactional
    public void createAwbWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = awbRepository.findAll().size();

        // Create the Awb with an existing ID
        awb.setId(1L);
        AwbDTO awbDTO = awbMapper.toDto(awb);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAwbMockMvc.perform(post("/api/awbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(awbDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Awb in the database
        List<Awb> awbList = awbRepository.findAll();
        assertThat(awbList).hasSize(databaseSizeBeforeCreate);

        // Validate the Awb in Elasticsearch
        verify(mockAwbSearchRepository, times(0)).save(awb);
    }

    @Test
    @Transactional
    public void checkAwbNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = awbRepository.findAll().size();
        // set the field null
        awb.setAwbNumber(null);

        // Create the Awb, which fails.
        AwbDTO awbDTO = awbMapper.toDto(awb);

        restAwbMockMvc.perform(post("/api/awbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(awbDTO)))
            .andExpect(status().isBadRequest());

        List<Awb> awbList = awbRepository.findAll();
        assertThat(awbList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAwbBarCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = awbRepository.findAll().size();
        // set the field null
        awb.setAwbBarCode(null);

        // Create the Awb, which fails.
        AwbDTO awbDTO = awbMapper.toDto(awb);

        restAwbMockMvc.perform(post("/api/awbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(awbDTO)))
            .andExpect(status().isBadRequest());

        List<Awb> awbList = awbRepository.findAll();
        assertThat(awbList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodIsRequired() throws Exception {
        int databaseSizeBeforeTest = awbRepository.findAll().size();
        // set the field null
        awb.setCod(null);

        // Create the Awb, which fails.
        AwbDTO awbDTO = awbMapper.toDto(awb);

        restAwbMockMvc.perform(post("/api/awbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(awbDTO)))
            .andExpect(status().isBadRequest());

        List<Awb> awbList = awbRepository.findAll();
        assertThat(awbList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = awbRepository.findAll().size();
        // set the field null
        awb.setCreateDate(null);

        // Create the Awb, which fails.
        AwbDTO awbDTO = awbMapper.toDto(awb);

        restAwbMockMvc.perform(post("/api/awbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(awbDTO)))
            .andExpect(status().isBadRequest());

        List<Awb> awbList = awbRepository.findAll();
        assertThat(awbList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsBrightAwbIsRequired() throws Exception {
        int databaseSizeBeforeTest = awbRepository.findAll().size();
        // set the field null
        awb.setIsBrightAwb(null);

        // Create the Awb, which fails.
        AwbDTO awbDTO = awbMapper.toDto(awb);

        restAwbMockMvc.perform(post("/api/awbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(awbDTO)))
            .andExpect(status().isBadRequest());

        List<Awb> awbList = awbRepository.findAll();
        assertThat(awbList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAwbs() throws Exception {
        // Initialize the database
        awbRepository.saveAndFlush(awb);

        // Get all the awbList
        restAwbMockMvc.perform(get("/api/awbs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(awb.getId().intValue())))
            .andExpect(jsonPath("$.[*].awbNumber").value(hasItem(DEFAULT_AWB_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].awbBarCode").value(hasItem(DEFAULT_AWB_BAR_CODE.toString())))
            .andExpect(jsonPath("$.[*].cod").value(hasItem(DEFAULT_COD.booleanValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].returnAwbNumber").value(hasItem(DEFAULT_RETURN_AWB_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].returnAwbBarCode").value(hasItem(DEFAULT_RETURN_AWB_BAR_CODE.toString())))
            .andExpect(jsonPath("$.[*].isBrightAwb").value(hasItem(DEFAULT_IS_BRIGHT_AWB.booleanValue())));
    }
    

    @Test
    @Transactional
    public void getAwb() throws Exception {
        // Initialize the database
        awbRepository.saveAndFlush(awb);

        // Get the awb
        restAwbMockMvc.perform(get("/api/awbs/{id}", awb.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(awb.getId().intValue()))
            .andExpect(jsonPath("$.awbNumber").value(DEFAULT_AWB_NUMBER.toString()))
            .andExpect(jsonPath("$.awbBarCode").value(DEFAULT_AWB_BAR_CODE.toString()))
            .andExpect(jsonPath("$.cod").value(DEFAULT_COD.booleanValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.returnAwbNumber").value(DEFAULT_RETURN_AWB_NUMBER.toString()))
            .andExpect(jsonPath("$.returnAwbBarCode").value(DEFAULT_RETURN_AWB_BAR_CODE.toString()))
            .andExpect(jsonPath("$.isBrightAwb").value(DEFAULT_IS_BRIGHT_AWB.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingAwb() throws Exception {
        // Get the awb
        restAwbMockMvc.perform(get("/api/awbs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAwb() throws Exception {
        // Initialize the database
        awbRepository.saveAndFlush(awb);

        int databaseSizeBeforeUpdate = awbRepository.findAll().size();

        // Update the awb
        Awb updatedAwb = awbRepository.findById(awb.getId()).get();
        // Disconnect from session so that the updates on updatedAwb are not directly saved in db
        em.detach(updatedAwb);
        updatedAwb
            .awbNumber(UPDATED_AWB_NUMBER)
            .awbBarCode(UPDATED_AWB_BAR_CODE)
            .cod(UPDATED_COD)
            .createDate(UPDATED_CREATE_DATE)
            .returnAwbNumber(UPDATED_RETURN_AWB_NUMBER)
            .returnAwbBarCode(UPDATED_RETURN_AWB_BAR_CODE)
            .isBrightAwb(UPDATED_IS_BRIGHT_AWB);
        AwbDTO awbDTO = awbMapper.toDto(updatedAwb);

        restAwbMockMvc.perform(put("/api/awbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(awbDTO)))
            .andExpect(status().isOk());

        // Validate the Awb in the database
        List<Awb> awbList = awbRepository.findAll();
        assertThat(awbList).hasSize(databaseSizeBeforeUpdate);
        Awb testAwb = awbList.get(awbList.size() - 1);
        assertThat(testAwb.getAwbNumber()).isEqualTo(UPDATED_AWB_NUMBER);
        assertThat(testAwb.getAwbBarCode()).isEqualTo(UPDATED_AWB_BAR_CODE);
        assertThat(testAwb.isCod()).isEqualTo(UPDATED_COD);
        assertThat(testAwb.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testAwb.getReturnAwbNumber()).isEqualTo(UPDATED_RETURN_AWB_NUMBER);
        assertThat(testAwb.getReturnAwbBarCode()).isEqualTo(UPDATED_RETURN_AWB_BAR_CODE);
        assertThat(testAwb.isIsBrightAwb()).isEqualTo(UPDATED_IS_BRIGHT_AWB);

        // Validate the Awb in Elasticsearch
        verify(mockAwbSearchRepository, times(1)).save(testAwb);
    }

    @Test
    @Transactional
    public void updateNonExistingAwb() throws Exception {
        int databaseSizeBeforeUpdate = awbRepository.findAll().size();

        // Create the Awb
        AwbDTO awbDTO = awbMapper.toDto(awb);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAwbMockMvc.perform(put("/api/awbs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(awbDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Awb in the database
        List<Awb> awbList = awbRepository.findAll();
        assertThat(awbList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Awb in Elasticsearch
        verify(mockAwbSearchRepository, times(0)).save(awb);
    }

    @Test
    @Transactional
    public void deleteAwb() throws Exception {
        // Initialize the database
        awbRepository.saveAndFlush(awb);

        int databaseSizeBeforeDelete = awbRepository.findAll().size();

        // Get the awb
        restAwbMockMvc.perform(delete("/api/awbs/{id}", awb.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Awb> awbList = awbRepository.findAll();
        assertThat(awbList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Awb in Elasticsearch
        verify(mockAwbSearchRepository, times(1)).deleteById(awb.getId());
    }

    @Test
    @Transactional
    public void searchAwb() throws Exception {
        // Initialize the database
        awbRepository.saveAndFlush(awb);
        when(mockAwbSearchRepository.search(queryStringQuery("id:" + awb.getId())))
            .thenReturn(Collections.singletonList(awb));
        // Search the awb
        restAwbMockMvc.perform(get("/api/_search/awbs?query=id:" + awb.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(awb.getId().intValue())))
            .andExpect(jsonPath("$.[*].awbNumber").value(hasItem(DEFAULT_AWB_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].awbBarCode").value(hasItem(DEFAULT_AWB_BAR_CODE.toString())))
            .andExpect(jsonPath("$.[*].cod").value(hasItem(DEFAULT_COD.booleanValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].returnAwbNumber").value(hasItem(DEFAULT_RETURN_AWB_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].returnAwbBarCode").value(hasItem(DEFAULT_RETURN_AWB_BAR_CODE.toString())))
            .andExpect(jsonPath("$.[*].isBrightAwb").value(hasItem(DEFAULT_IS_BRIGHT_AWB.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Awb.class);
        Awb awb1 = new Awb();
        awb1.setId(1L);
        Awb awb2 = new Awb();
        awb2.setId(awb1.getId());
        assertThat(awb1).isEqualTo(awb2);
        awb2.setId(2L);
        assertThat(awb1).isNotEqualTo(awb2);
        awb1.setId(null);
        assertThat(awb1).isNotEqualTo(awb2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AwbDTO.class);
        AwbDTO awbDTO1 = new AwbDTO();
        awbDTO1.setId(1L);
        AwbDTO awbDTO2 = new AwbDTO();
        assertThat(awbDTO1).isNotEqualTo(awbDTO2);
        awbDTO2.setId(awbDTO1.getId());
        assertThat(awbDTO1).isEqualTo(awbDTO2);
        awbDTO2.setId(2L);
        assertThat(awbDTO1).isNotEqualTo(awbDTO2);
        awbDTO1.setId(null);
        assertThat(awbDTO1).isNotEqualTo(awbDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(awbMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(awbMapper.fromId(null)).isNull();
    }
}
