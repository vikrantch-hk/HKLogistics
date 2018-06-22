package com.hk.logistics.repository.search;

import com.hk.logistics.domain.RegionType;
import com.hk.logistics.service.dto.RegionTypeDTO;

import java.util.List;

import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RegionType entity.
 */
public interface RegionTypeSearchRepository extends ElasticsearchRepository<RegionType, Long> {

	List<RegionType> findByPriority(String priority);
	
	List<RegionType> findByNameLike(String name);
}
