package com.hk.logistics.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.hk.logistics.domain.CourierChannel;
import com.hk.logistics.domain.*; // for static metamodels
import com.hk.logistics.repository.CourierChannelRepository;
import com.hk.logistics.repository.search.CourierChannelSearchRepository;
import com.hk.logistics.service.dto.CourierChannelCriteria;

import com.hk.logistics.service.dto.CourierChannelDTO;
import com.hk.logistics.service.mapper.CourierChannelMapper;

/**
 * Service for executing complex queries for CourierChannel entities in the database.
 * The main input is a {@link CourierChannelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CourierChannelDTO} or a {@link Page} of {@link CourierChannelDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CourierChannelQueryService extends QueryService<CourierChannel> {

    private final Logger log = LoggerFactory.getLogger(CourierChannelQueryService.class);

    private final CourierChannelRepository courierChannelRepository;

    private final CourierChannelMapper courierChannelMapper;

    private final CourierChannelSearchRepository courierChannelSearchRepository;

    public CourierChannelQueryService(CourierChannelRepository courierChannelRepository, CourierChannelMapper courierChannelMapper, CourierChannelSearchRepository courierChannelSearchRepository) {
        this.courierChannelRepository = courierChannelRepository;
        this.courierChannelMapper = courierChannelMapper;
        this.courierChannelSearchRepository = courierChannelSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CourierChannelDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CourierChannelDTO> findByCriteria(CourierChannelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CourierChannel> specification = createSpecification(criteria);
        return courierChannelMapper.toDto(courierChannelRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CourierChannelDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CourierChannelDTO> findByCriteria(CourierChannelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CourierChannel> specification = createSpecification(criteria);
        return courierChannelRepository.findAll(specification, page)
            .map(courierChannelMapper::toDto);
    }

    /**
     * Function to convert CourierChannelCriteria to a {@link Specification}
     */
    private Specification<CourierChannel> createSpecification(CourierChannelCriteria criteria) {
        Specification<CourierChannel> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), CourierChannel_.id));
            }
            if (criteria.getMinWeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMinWeight(), CourierChannel_.minWeight));
            }
            if (criteria.getMaxWeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxWeight(), CourierChannel_.maxWeight));
            }
            if (criteria.getNatureOfProduct() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNatureOfProduct(), CourierChannel_.natureOfProduct));
            }
            if (criteria.getVendorWHCourierMappingId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getVendorWHCourierMappingId(), CourierChannel_.vendorWHCourierMappings, VendorWHCourierMapping_.id));
            }
            if (criteria.getChannelId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getChannelId(), CourierChannel_.channel, Channel_.id));
            }
            if (criteria.getCourierId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCourierId(), CourierChannel_.couriers, Courier_.id));
            }
        }
        return specification;
    }

}
