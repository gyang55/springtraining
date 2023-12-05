package ca.emonster.training.ecommerce.web.rest;

import ca.emonster.training.ecommerce.repository.TblDimensionRepository;
import ca.emonster.training.ecommerce.service.TblDimensionQueryService;
import ca.emonster.training.ecommerce.service.TblDimensionService;
import ca.emonster.training.ecommerce.service.criteria.TblDimensionCriteria;
import ca.emonster.training.ecommerce.service.dto.TblDimensionDTO;
import ca.emonster.training.ecommerce.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ca.emonster.training.ecommerce.domain.TblDimension}.
 */
@RestController
@RequestMapping("/api")
public class TblDimensionResource {

    private final Logger log = LoggerFactory.getLogger(TblDimensionResource.class);

    private static final String ENTITY_NAME = "tblDimension";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TblDimensionService tblDimensionService;

    private final TblDimensionRepository tblDimensionRepository;

    private final TblDimensionQueryService tblDimensionQueryService;

    public TblDimensionResource(
        TblDimensionService tblDimensionService,
        TblDimensionRepository tblDimensionRepository,
        TblDimensionQueryService tblDimensionQueryService
    ) {
        this.tblDimensionService = tblDimensionService;
        this.tblDimensionRepository = tblDimensionRepository;
        this.tblDimensionQueryService = tblDimensionQueryService;
    }

    /**
     * {@code POST  /tbl-dimensions} : Create a new tblDimension.
     *
     * @param tblDimensionDTO the tblDimensionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tblDimensionDTO, or with status {@code 400 (Bad Request)} if the tblDimension has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tbl-dimensions")
    public ResponseEntity<TblDimensionDTO> createTblDimension(@Valid @RequestBody TblDimensionDTO tblDimensionDTO)
        throws URISyntaxException {
        log.debug("REST request to save TblDimension : {}", tblDimensionDTO);
        if (tblDimensionDTO.getId() != null) {
            throw new BadRequestAlertException("A new tblDimension cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TblDimensionDTO result = tblDimensionService.save(tblDimensionDTO);
        return ResponseEntity
            .created(new URI("/api/tbl-dimensions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tbl-dimensions/:id} : Updates an existing tblDimension.
     *
     * @param id the id of the tblDimensionDTO to save.
     * @param tblDimensionDTO the tblDimensionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblDimensionDTO,
     * or with status {@code 400 (Bad Request)} if the tblDimensionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tblDimensionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tbl-dimensions/{id}")
    public ResponseEntity<TblDimensionDTO> updateTblDimension(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TblDimensionDTO tblDimensionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TblDimension : {}, {}", id, tblDimensionDTO);
        if (tblDimensionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblDimensionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblDimensionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TblDimensionDTO result = tblDimensionService.save(tblDimensionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblDimensionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tbl-dimensions/:id} : Partial updates given fields of an existing tblDimension, field will ignore if it is null
     *
     * @param id the id of the tblDimensionDTO to save.
     * @param tblDimensionDTO the tblDimensionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblDimensionDTO,
     * or with status {@code 400 (Bad Request)} if the tblDimensionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tblDimensionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tblDimensionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tbl-dimensions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TblDimensionDTO> partialUpdateTblDimension(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TblDimensionDTO tblDimensionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TblDimension partially : {}, {}", id, tblDimensionDTO);
        if (tblDimensionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblDimensionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblDimensionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TblDimensionDTO> result = tblDimensionService.partialUpdate(tblDimensionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblDimensionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tbl-dimensions} : get all the tblDimensions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tblDimensions in body.
     */
    @GetMapping("/tbl-dimensions")
    public ResponseEntity<List<TblDimensionDTO>> getAllTblDimensions(
        TblDimensionCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TblDimensions by criteria: {}", criteria);
        Page<TblDimensionDTO> page = tblDimensionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tbl-dimensions/count} : count all the tblDimensions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tbl-dimensions/count")
    public ResponseEntity<Long> countTblDimensions(TblDimensionCriteria criteria) {
        log.debug("REST request to count TblDimensions by criteria: {}", criteria);
        return ResponseEntity.ok().body(tblDimensionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tbl-dimensions/:id} : get the "id" tblDimension.
     *
     * @param id the id of the tblDimensionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tblDimensionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tbl-dimensions/{id}")
    public ResponseEntity<TblDimensionDTO> getTblDimension(@PathVariable Long id) {
        log.debug("REST request to get TblDimension : {}", id);
        Optional<TblDimensionDTO> tblDimensionDTO = tblDimensionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tblDimensionDTO);
    }

    /**
     * {@code DELETE  /tbl-dimensions/:id} : delete the "id" tblDimension.
     *
     * @param id the id of the tblDimensionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tbl-dimensions/{id}")
    public ResponseEntity<Void> deleteTblDimension(@PathVariable Long id) {
        log.debug("REST request to delete TblDimension : {}", id);
        tblDimensionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
