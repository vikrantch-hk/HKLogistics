package com.hk.logistics.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of CourierChannelSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CourierChannelSearchRepositoryMockConfiguration {

    @MockBean
    private CourierChannelSearchRepository mockCourierChannelSearchRepository;

}
