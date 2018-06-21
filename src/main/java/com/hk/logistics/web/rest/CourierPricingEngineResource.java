package com.hk.logistics.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hk.logistics.service.CourierPricingEngineService;
import com.hk.logistics.web.rest.errors.BadRequestAlertException;
import com.hk.logistics.web.rest.util.HeaderUtil;
import com.hk.logistics.service.dto.CourierPricingEngineDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CourierPricingEngine.
 */
@RestController
@RequestMapping("/api")
public class CourierPricingEngineResource {

    private final Logger log = LoggerFactory.getLogger(CourierPricingEngineResource.class);

    private static final String ENTITY_NAME = "courierPricingEngine";

    private final CourierPricingEngineService courierPricingEngineService;

    public CourierPricingEngineResource(CourierPricingEngineService courierPricingEngineService) {
        this.courierPricingEngineService = courierPricingEngineService;
    }

    /**
     * POST  /courier-pricing-engines : Create a new courierPricingEngine.
     *
     * @param courierPricingEngineDTO the courierPricingEngineDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new courierPricingEngineDTO, or with status 400 (Bad Request) if the courierPricingEngine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/courier-pricing-engines")
    @Timed
    public ResponseEntity<CourierPricingEngineDTO> createCourierPricingEngine(@Valid @RequestBody CourierPricingEngineDTO courierPricingEngineDTO) throws URISyntaxException {
        log.debug("REST request to save CourierPricingEngine : {}", courierPricingEngineDTO);
        if (courierPricingEngineDTO.getId() != null) {
            throw new BadRequestAlertException("A new courierPricingEngine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourierPricingEngineDTO result = courierPricingEngineService.save(courierPricingEngineDTO);
        return ResponseEntity.created(new URI("/api/courier-pricing-engines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /courier-pricing-engines : Updates an existing courierPricingEngine.
     *
     * @param courierPricingEngineDTO the courierPricingEngineDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated courierPricingEngineDTO,
     * or with status 400 (Bad Request) if the courierPricingEngineDTO is not valid,
     * or with status 500 (Internal Server Error) if the courierPricingEngineDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/courier-pricing-engines")
    @Timed
    public ResponseEntity<CourierPricingEngineDTO> updateCourierPricingEngine(@Valid @RequestBody CourierPricingEngineDTO courierPricingEngineDTO) throws URISyntaxException {
        log.debug("REST request to update CourierPricingEngine : {}", courierPricingEngineDTO);
        if (courierPricingEngineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CourierPricingEngineDTO result = courierPricingEngineService.save(courierPricingEngineDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, courierPricingEngineDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /courier-pricing-engines : get all the courierPricingEngines.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of courierPricingEngines in body
     */
    @GetMapping("/courier-pricing-engines")
    @Timed
    public List<CourierPricingEngineDTO> getAllCourierPricingEngines() {
        log.debug("REST request to get all CourierPricingEngines");
        return courierPricingEngineService.findAll();
    }

    /**
     * GET  /courier-pricing-engines/:id : get the "id" courierPricingEngine.
     *
     * @param id the id of the courierPricingEngineDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the courierPricingEngineDTO, or with status 404 (Not Found)
     */
    @GetMapping("/courier-pricing-engines/{id}")
    @Timed
    public ResponseEntity<CourierPricingEngineDTO> getCourierPricingEngine(@PathVariable Long id) {
        log.debug("REST request to get CourierPricingEngine : {}", id);
        Optional<CourierPricingEngineDTO> courierPricingEngineDTO = courierPricingEngineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courierPricingEngineDTO);
    }

    /**
     * DELETE  /courier-pricing-engines/:id : delete the "id" courierPricingEngine.
     *
     * @param id the id of the courierPricingEngineDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/courier-pricing-engines/{id}")
    @Timed
    public ResponseEntity<Void> deleteCourierPricingEngine(@PathVariable Long id) {
        log.debug("REST request to delete CourierPricingEngine : {}", id);
        courierPricingEngineService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/courier-pricing-engines?query=:query : search for the courierPricingEngine corresponding
     * to the query.
     *
     * @param query the query of the courierPricingEngine search
     * @return the result of the search
     */
    @GetMapping("/_search/courier-pricing-engines")
    @Timed
    public List<CourierPricingEngineDTO> searchCourierPricingEngines(@RequestParam String query) {
        log.debug("REST request to search CourierPricingEngines for query {}", query);
        return courierPricingEngineService.search(query);
    }

}
