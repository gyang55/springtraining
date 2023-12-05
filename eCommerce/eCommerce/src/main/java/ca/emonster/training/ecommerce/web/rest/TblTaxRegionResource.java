package ca.emonster.training.ecommerce.web.rest;

import ca.emonster.training.ecommerce.repository.TblTaxRegionRepository;
import ca.emonster.training.ecommerce.service.TblTaxRegionQueryService;
import ca.emonster.training.ecommerce.service.TblTaxRegionService;
import ca.emonster.training.ecommerce.service.criteria.TblTaxRegionCriteria;
import ca.emonster.training.ecommerce.service.dto.TblTaxRegionDTO;
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
 * REST controller for managing {@link ca.emonster.training.ecommerce.domain.TblTaxRegion}.
 */
@RestController
@RequestMapping("/api")
public class TblTaxRegionResource {

    private final Logger log = LoggerFactory.getLogger(TblTaxRegionResource.class);

    private static final String ENTITY_NAME = "tblTaxRegion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TblTaxRegionService tblTaxRegionService;

    private final TblTaxRegionRepository tblTaxRegionRepository;

    private final TblTaxRegionQueryService tblTaxRegionQueryService;

    public TblTaxRegionResource(
        TblTaxRegionService tblTaxRegionService,
        TblTaxRegionRepository tblTaxRegionRepository,
        TblTaxRegionQueryService tblTaxRegionQueryService
    ) {
        this.tblTaxRegionService = tblTaxRegionService;
        this.tblTaxRegionRepository = tblTaxRegionRepository;
        this.tblTaxRegionQueryService = tblTaxRegionQueryService;
    }

    /**
     * {@code POST  /tbl-tax-regions} : Create a new tblTaxRegion.
     *
     * @param tblTaxRegionDTO the tblTaxRegionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tblTaxRegionDTO, or with status {@code 400 (Bad Request)} if the tblTaxRegion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tbl-tax-regions")
    public ResponseEntity<TblTaxRegionDTO> createTblTaxRegion(@Valid @RequestBody TblTaxRegionDTO tblTaxRegionDTO)
        throws URISyntaxException {
        log.debug("REST request to save TblTaxRegion : {}", tblTaxRegionDTO);
        if (tblTaxRegionDTO.getId() != null) {
            throw new BadRequestAlertException("A new tblTaxRegion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TblTaxRegionDTO result = tblTaxRegionService.save(tblTaxRegionDTO);
        return ResponseEntity
            .created(new URI("/api/tbl-tax-regions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tbl-tax-regions/:id} : Updates an existing tblTaxRegion.
     *
     * @param id the id of the tblTaxRegionDTO to save.
     * @param tblTaxRegionDTO the tblTaxRegionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblTaxRegionDTO,
     * or with status {@code 400 (Bad Request)} if the tblTaxRegionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tblTaxRegionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tbl-tax-regions/{id}")
    public ResponseEntity<TblTaxRegionDTO> updateTblTaxRegion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TblTaxRegionDTO tblTaxRegionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TblTaxRegion : {}, {}", id, tblTaxRegionDTO);
        if (tblTaxRegionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblTaxRegionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblTaxRegionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TblTaxRegionDTO result = tblTaxRegionService.save(tblTaxRegionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblTaxRegionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tbl-tax-regions/:id} : Partial updates given fields of an existing tblTaxRegion, field will ignore if it is null
     *
     * @param id the id of the tblTaxRegionDTO to save.
     * @param tblTaxRegionDTO the tblTaxRegionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblTaxRegionDTO,
     * or with status {@code 400 (Bad Request)} if the tblTaxRegionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tblTaxRegionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tblTaxRegionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tbl-tax-regions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TblTaxRegionDTO> partialUpdateTblTaxRegion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TblTaxRegionDTO tblTaxRegionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TblTaxRegion partially : {}, {}", id, tblTaxRegionDTO);
        if (tblTaxRegionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblTaxRegionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblTaxRegionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TblTaxRegionDTO> result = tblTaxRegionService.partialUpdate(tblTaxRegionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblTaxRegionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tbl-tax-regions} : get all the tblTaxRegions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tblTaxRegions in body.
     */
    @GetMapping("/tbl-tax-regions")
    public ResponseEntity<List<TblTaxRegionDTO>> getAllTblTaxRegions(
        TblTaxRegionCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TblTaxRegions by criteria: {}", criteria);
        Page<TblTaxRegionDTO> page = tblTaxRegionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tbl-tax-regions/count} : count all the tblTaxRegions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tbl-tax-regions/count")
    public ResponseEntity<Long> countTblTaxRegions(TblTaxRegionCriteria criteria) {
        log.debug("REST request to count TblTaxRegions by criteria: {}", criteria);
        return ResponseEntity.ok().body(tblTaxRegionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tbl-tax-regions/:id} : get the "id" tblTaxRegion.
     *
     * @param id the id of the tblTaxRegionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tblTaxRegionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tbl-tax-regions/{id}")
    public ResponseEntity<TblTaxRegionDTO> getTblTaxRegion(@PathVariable Long id) {
        log.debug("REST request to get TblTaxRegion : {}", id);
        Optional<TblTaxRegionDTO> tblTaxRegionDTO = tblTaxRegionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tblTaxRegionDTO);
    }

    /**
     * {@code DELETE  /tbl-tax-regions/:id} : delete the "id" tblTaxRegion.
     *
     * @param id the id of the tblTaxRegionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tbl-tax-regions/{id}")
    public ResponseEntity<Void> deleteTblTaxRegion(@PathVariable Long id) {
        log.debug("REST request to delete TblTaxRegion : {}", id);
        tblTaxRegionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
