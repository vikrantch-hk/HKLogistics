package com.hk.logistics.web.rest;

import com.hk.logistics.HkLogisticsApp;

import com.hk.logistics.domain.SourceDestinationMapping;
import com.hk.logistics.repository.SourceDestinationMappingRepository;
import com.hk.logistics.repository.search.SourceDestinationMappingSearchRepository;
import com.hk.logistics.service.SourceDestinationMappingService;
import com.hk.logistics.service.dto.SourceDestinationMappingDTO;
import com.hk.logistics.service.mapper.SourceDestinationMappingMapper;
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
 * Test class for the SourceDestinationMappingResource REST controller.
 *
 * @see SourceDestinationMappingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HkLogisticsApp.class)
public class SourceDestinationMappingResourceIntTest {

    private static final String DEFAULT_SOURCE_PINCODE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_PINCODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINATION_PINCODE = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION_PINCODE = "BBBBBBBBBB";

    @Autowired
    private SourceDestinationMappingRepository sourceDestinationMappingRepository;


    @Autowired
    private SourceDestinationMappingMapper sourceDestinationMappingMapper;
    

    @Autowired
    private SourceDestinationMappingService sourceDestinationMappingService;

    /**
     * This repository is mocked in the com.hk.logistics.repository.search test package.
     *
     * @see com.hk.logistics.repository.search.SourceDestinationMappingSearchRepositoryMockConfiguration
     */
    @Autowired
    private SourceDestinationMappingSearchRepository mockSourceDestinationMappingSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSourceDestinationMappingMockMvc;

    private SourceDestinationMapping sourceDestinationMapping;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SourceDestinationMappingResource sourceDestinationMappingResource = new SourceDestinationMappingResource(sourceDestinationMappingService);
        this.restSourceDestinationMappingMockMvc = MockMvcBuilders.standaloneSetup(sourceDestinationMappingResource)
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
    public static SourceDestinationMapping createEntity(EntityManager em) {
        SourceDestinationMapping sourceDestinationMapping = new SourceDestinationMapping()
            .sourcePincode(DEFAULT_SOURCE_PINCODE)
            .destinationPincode(DEFAULT_DESTINATION_PINCODE);
        return sourceDestinationMapping;
    }

    @Before
    public void initTest() {
        sourceDestinationMapping = createEntity(em);
    }

    @Test
    @Transactional
    public void createSourceDestinationMapping() throws Exception {
        int databaseSizeBeforeCreate = sourceDestinationMappingRepository.findAll().size();

        // Create the SourceDestinationMapping
        SourceDestinationMappingDTO sourceDestinationMappingDTO = sourceDestinationMappingMapper.toDto(sourceDestinationMapping);
        restSourceDestinationMappingMockMvc.perform(post("/api/source-destination-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceDestinationMappingDTO)))
            .andExpect(status().isCreated());

        // Validate the SourceDestinationMapping in the database
        List<SourceDestinationMapping> sourceDestinationMappingList = sourceDestinationMappingRepository.findAll();
        assertThat(sourceDestinationMappingList).hasSize(databaseSizeBeforeCreate + 1);
        SourceDestinationMapping testSourceDestinationMapping = sourceDestinationMappingList.get(sourceDestinationMappingList.size() - 1);
        assertThat(testSourceDestinationMapping.getSourcePincode()).isEqualTo(DEFAULT_SOURCE_PINCODE);
        assertThat(testSourceDestinationMapping.getDestinationPincode()).isEqualTo(DEFAULT_DESTINATION_PINCODE);

        // Validate the SourceDestinationMapping in Elasticsearch
        verify(mockSourceDestinationMappingSearchRepository, times(1)).save(testSourceDestinationMapping);
    }

    @Test
    @Transactional
    public void createSourceDestinationMappingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sourceDestinationMappingRepository.findAll().size();

        // Create the SourceDestinationMapping with an existing ID
        sourceDestinationMapping.setId(1L);
        SourceDestinationMappingDTO sourceDestinationMappingDTO = sourceDestinationMappingMapper.toDto(sourceDestinationMapping);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSourceDestinationMappingMockMvc.perform(post("/api/source-destination-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceDestinationMappingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SourceDestinationMapping in the database
        List<SourceDestinationMapping> sourceDestinationMappingList = sourceDestinationMappingRepository.findAll();
        assertThat(sourceDestinationMappingList).hasSize(databaseSizeBeforeCreate);

        // Validate the SourceDestinationMapping in Elasticsearch
        verify(mockSourceDestinationMappingSearchRepository, times(0)).save(sourceDestinationMapping);
    }

    @Test
    @Transactional
    public void checkSourcePincodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sourceDestinationMappingRepository.findAll().size();
        // set the field null
        sourceDestinationMapping.setSourcePincode(null);

        // Create the SourceDestinationMapping, which fails.
        SourceDestinationMappingDTO sourceDestinationMappingDTO = sourceDestinationMappingMapper.toDto(sourceDestinationMapping);

        restSourceDestinationMappingMockMvc.perform(post("/api/source-destination-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceDestinationMappingDTO)))
            .andExpect(status().isBadRequest());

        List<SourceDestinationMapping> sourceDestinationMappingList = sourceDestinationMappingRepository.findAll();
        assertThat(sourceDestinationMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDestinationPincodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sourceDestinationMappingRepository.findAll().size();
        // set the field null
        sourceDestinationMapping.setDestinationPincode(null);

        // Create the SourceDestinationMapping, which fails.
        SourceDestinationMappingDTO sourceDestinationMappingDTO = sourceDestinationMappingMapper.toDto(sourceDestinationMapping);

        restSourceDestinationMappingMockMvc.perform(post("/api/source-destination-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceDestinationMappingDTO)))
            .andExpect(status().isBadRequest());

        List<SourceDestinationMapping> sourceDestinationMappingList = sourceDestinationMappingRepository.findAll();
        assertThat(sourceDestinationMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSourceDestinationMappings() throws Exception {
        // Initialize the database
        sourceDestinationMappingRepository.saveAndFlush(sourceDestinationMapping);

        // Get all the sourceDestinationMappingList
        restSourceDestinationMappingMockMvc.perform(get("/api/source-destination-mappings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourceDestinationMapping.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourcePincode").value(hasItem(DEFAULT_SOURCE_PINCODE.toString())))
            .andExpect(jsonPath("$.[*].destinationPincode").value(hasItem(DEFAULT_DESTINATION_PINCODE.toString())));
    }
    

    @Test
    @Transactional
    public void getSourceDestinationMapping() throws Exception {
        // Initialize the database
        sourceDestinationMappingRepository.saveAndFlush(sourceDestinationMapping);

        // Get the sourceDestinationMapping
        restSourceDestinationMappingMockMvc.perform(get("/api/source-destination-mappings/{id}", sourceDestinationMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sourceDestinationMapping.getId().intValue()))
            .andExpect(jsonPath("$.sourcePincode").value(DEFAULT_SOURCE_PINCODE.toString()))
            .andExpect(jsonPath("$.destinationPincode").value(DEFAULT_DESTINATION_PINCODE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingSourceDestinationMapping() throws Exception {
        // Get the sourceDestinationMapping
        restSourceDestinationMappingMockMvc.perform(get("/api/source-destination-mappings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSourceDestinationMapping() throws Exception {
        // Initialize the database
        sourceDestinationMappingRepository.saveAndFlush(sourceDestinationMapping);

        int databaseSizeBeforeUpdate = sourceDestinationMappingRepository.findAll().size();

        // Update the sourceDestinationMapping
        SourceDestinationMapping updatedSourceDestinationMapping = sourceDestinationMappingRepository.findById(sourceDestinationMapping.getId()).get();
        // Disconnect from session so that the updates on updatedSourceDestinationMapping are not directly saved in db
        em.detach(updatedSourceDestinationMapping);
        updatedSourceDestinationMapping
            .sourcePincode(UPDATED_SOURCE_PINCODE)
            .destinationPincode(UPDATED_DESTINATION_PINCODE);
        SourceDestinationMappingDTO sourceDestinationMappingDTO = sourceDestinationMappingMapper.toDto(updatedSourceDestinationMapping);

        restSourceDestinationMappingMockMvc.perform(put("/api/source-destination-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceDestinationMappingDTO)))
            .andExpect(status().isOk());

        // Validate the SourceDestinationMapping in the database
        List<SourceDestinationMapping> sourceDestinationMappingList = sourceDestinationMappingRepository.findAll();
        assertThat(sourceDestinationMappingList).hasSize(databaseSizeBeforeUpdate);
        SourceDestinationMapping testSourceDestinationMapping = sourceDestinationMappingList.get(sourceDestinationMappingList.size() - 1);
        assertThat(testSourceDestinationMapping.getSourcePincode()).isEqualTo(UPDATED_SOURCE_PINCODE);
        assertThat(testSourceDestinationMapping.getDestinationPincode()).isEqualTo(UPDATED_DESTINATION_PINCODE);

        // Validate the SourceDestinationMapping in Elasticsearch
        verify(mockSourceDestinationMappingSearchRepository, times(1)).save(testSourceDestinationMapping);
    }

    @Test
    @Transactional
    public void updateNonExistingSourceDestinationMapping() throws Exception {
        int databaseSizeBeforeUpdate = sourceDestinationMappingRepository.findAll().size();

        // Create the SourceDestinationMapping
        SourceDestinationMappingDTO sourceDestinationMappingDTO = sourceDestinationMappingMapper.toDto(sourceDestinationMapping);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSourceDestinationMappingMockMvc.perform(put("/api/source-destination-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceDestinationMappingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SourceDestinationMapping in the database
        List<SourceDestinationMapping> sourceDestinationMappingList = sourceDestinationMappingRepository.findAll();
        assertThat(sourceDestinationMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SourceDestinationMapping in Elasticsearch
        verify(mockSourceDestinationMappingSearchRepository, times(0)).save(sourceDestinationMapping);
    }

    @Test
    @Transactional
    public void deleteSourceDestinationMapping() throws Exception {
        // Initialize the database
        sourceDestinationMappingRepository.saveAndFlush(sourceDestinationMapping);

        int databaseSizeBeforeDelete = sourceDestinationMappingRepository.findAll().size();

        // Get the sourceDestinationMapping
        restSourceDestinationMappingMockMvc.perform(delete("/api/source-destination-mappings/{id}", sourceDestinationMapping.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SourceDestinationMapping> sourceDestinationMappingList = sourceDestinationMappingRepository.findAll();
        assertThat(sourceDestinationMappingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SourceDestinationMapping in Elasticsearch
        verify(mockSourceDestinationMappingSearchRepository, times(1)).deleteById(sourceDestinationMapping.getId());
    }

    @Test
    @Transactional
    public void searchSourceDestinationMapping() throws Exception {
        // Initialize the database
        sourceDestinationMappingRepository.saveAndFlush(sourceDestinationMapping);
        when(mockSourceDestinationMappingSearchRepository.search(queryStringQuery("id:" + sourceDestinationMapping.getId())))
            .thenReturn(Collections.singletonList(sourceDestinationMapping));
        // Search the sourceDestinationMapping
        restSourceDestinationMappingMockMvc.perform(get("/api/_search/source-destination-mappings?query=id:" + sourceDestinationMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourceDestinationMapping.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourcePincode").value(hasItem(DEFAULT_SOURCE_PINCODE.toString())))
            .andExpect(jsonPath("$.[*].destinationPincode").value(hasItem(DEFAULT_DESTINATION_PINCODE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SourceDestinationMapping.class);
        SourceDestinationMapping sourceDestinationMapping1 = new SourceDestinationMapping();
        sourceDestinationMapping1.setId(1L);
        SourceDestinationMapping sourceDestinationMapping2 = new SourceDestinationMapping();
        sourceDestinationMapping2.setId(sourceDestinationMapping1.getId());
        assertThat(sourceDestinationMapping1).isEqualTo(sourceDestinationMapping2);
        sourceDestinationMapping2.setId(2L);
        assertThat(sourceDestinationMapping1).isNotEqualTo(sourceDestinationMapping2);
        sourceDestinationMapping1.setId(null);
        assertThat(sourceDestinationMapping1).isNotEqualTo(sourceDestinationMapping2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SourceDestinationMappingDTO.class);
        SourceDestinationMappingDTO sourceDestinationMappingDTO1 = new SourceDestinationMappingDTO();
        sourceDestinationMappingDTO1.setId(1L);
        SourceDestinationMappingDTO sourceDestinationMappingDTO2 = new SourceDestinationMappingDTO();
        assertThat(sourceDestinationMappingDTO1).isNotEqualTo(sourceDestinationMappingDTO2);
        sourceDestinationMappingDTO2.setId(sourceDestinationMappingDTO1.getId());
        assertThat(sourceDestinationMappingDTO1).isEqualTo(sourceDestinationMappingDTO2);
        sourceDestinationMappingDTO2.setId(2L);
        assertThat(sourceDestinationMappingDTO1).isNotEqualTo(sourceDestinationMappingDTO2);
        sourceDestinationMappingDTO1.setId(null);
        assertThat(sourceDestinationMappingDTO1).isNotEqualTo(sourceDestinationMappingDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(sourceDestinationMappingMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(sourceDestinationMappingMapper.fromId(null)).isNull();
    }
}
