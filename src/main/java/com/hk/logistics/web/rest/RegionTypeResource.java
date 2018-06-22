package com.hk.logistics.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hk.logistics.security.AuthoritiesConstants;
import com.hk.logistics.service.RegionTypeService;
import com.hk.logistics.web.rest.errors.BadRequestAlertException;
import com.hk.logistics.web.rest.util.HeaderUtil;
import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import com.hk.logistics.service.dto.RegionTypeDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.StreamSupport;


import static org.elasticsearch.index.query.QueryBuilders.*;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;

/**
 * REST controller for managing RegionType.
 */
@RestController
@RequestMapping("/api")
public class RegionTypeResource {

    private final Logger log = LoggerFactory.getLogger(RegionTypeResource.class);

    private static final String ENTITY_NAME = "regionType";

    private final RegionTypeService regionTypeService;
    
    private int batchSize = 100;

    public RegionTypeResource(RegionTypeService regionTypeService) {
        this.regionTypeService = regionTypeService;
    }

    /**
     * POST  /region-types : Create a new regionType.
     *
     * @param regionTypeDTO the regionTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new regionTypeDTO, or with status 400 (Bad Request) if the regionType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/region-types")
    @Timed
    public ResponseEntity<RegionTypeDTO> createRegionType(@RequestBody RegionTypeDTO regionTypeDTO) throws URISyntaxException {
        log.debug("REST request to save RegionType : {}", regionTypeDTO);
        if (regionTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new regionType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RegionTypeDTO result = regionTypeService.save(regionTypeDTO);
        return ResponseEntity.created(new URI("/api/region-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /region-types : Updates an existing regionType.
     *
     * @param regionTypeDTO the regionTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated regionTypeDTO,
     * or with status 400 (Bad Request) if the regionTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the regionTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/region-types")
    @Timed
    public ResponseEntity<RegionTypeDTO> updateRegionType(@RequestBody RegionTypeDTO regionTypeDTO) throws URISyntaxException {
        log.debug("REST request to update RegionType : {}", regionTypeDTO);
        if (regionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RegionTypeDTO result = regionTypeService.save(regionTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, regionTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /region-types : get all the regionTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of regionTypes in body
     */
    @GetMapping("/region-types")
    @Timed
    public List<RegionTypeDTO> getAllRegionTypes() {
        log.debug("REST request to get all RegionTypes");
        return regionTypeService.findAll();
    }

    /**
     * GET  /region-types/:id : get the "id" regionType.
     *
     * @param id the id of the regionTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the regionTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/region-types/{id}")
    @Timed
    public ResponseEntity<RegionTypeDTO> getRegionType(@PathVariable Long id) {
        log.debug("REST request to get RegionType : {}", id);
        Optional<RegionTypeDTO> regionTypeDTO = regionTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(regionTypeDTO);
    }

    /**
     * DELETE  /region-types/:id : delete the "id" regionType.
     *
     * @param id the id of the regionTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/region-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteRegionType(@PathVariable Long id) {
        log.debug("REST request to delete RegionType : {}", id);
        regionTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/region-types?query=:query : search for the regionType corresponding
     * to the query.
     *
     * @param query the query of the regionType search
     * @return the result of the search
     */
    @GetMapping("/_search/region-types")
    @Timed
    public List<RegionTypeDTO> searchRegionTypes(@RequestParam String query) {
        log.debug("REST request to search RegionTypes for query {}", query);
        return regionTypeService.search(query);
    }
    
    @RequestMapping(value = "/region-types/upload", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('"+AuthoritiesConstants.MANAGER+"')")
    public @ResponseBody ResponseEntity<RegionTypeDTO> handleFileUpload(
			@RequestParam(value = "file", required = false) MultipartFile file) throws URISyntaxException {
		RegionTypeDTO result = null;
		try {
			List<RegionTypeDTO> vendorList = Poiji.fromExcel(new ByteArrayInputStream(file.getBytes()),
					PoijiExcelType.XLSX, RegionTypeDTO.class);

			Future<List<RegionTypeDTO>> savedEntities;

			if (vendorList.size() > 0) {

				savedEntities = bulkSave(vendorList);
				return ResponseEntity.created(new URI("/api/upload/"))
						// .headers(HeaderUtil.createEntityCreationAlert("Vendor", null))
						.body(result);

			} else {
				throw new BadRequestAlertException("A new vendor File cannot be empty", ENTITY_NAME, "idexists");
			}
		} catch (RuntimeException | IOException e) {
			log.error("Error while uploading.", e);
			throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "idexists");
		} catch (Exception e) {
			log.error("Error while uploading.", e);
			throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "idexists");
		}
	}
    @Async
	public Future<List<RegionTypeDTO>> bulkSave(List<RegionTypeDTO> entities) {
        int size = entities.size();
        List<RegionTypeDTO> savedEntities = new ArrayList<>(size);
        try {
            for (int i = 0; i < size; i += batchSize) {
                int toIndex = i + (((i + batchSize) < size) ? batchSize : size - i);
                savedEntities.addAll(processBatch(entities.subList(i, toIndex)));
                
            }
        } catch (Exception ignored) {
            // or do something...  
        }
        
        if(savedEntities.size()!=entities.size())
		{
			log.error("few entities are not saved");
		}
        else
        {
        	log.debug("entities are saved");
        }
        
        return new AsyncResult<List<RegionTypeDTO>>(savedEntities);
    }
	
    protected List<RegionTypeDTO> processBatch(List<RegionTypeDTO> batch) {
        List<RegionTypeDTO> list = regionTypeService.upload(batch);
        return list;
    }
}
