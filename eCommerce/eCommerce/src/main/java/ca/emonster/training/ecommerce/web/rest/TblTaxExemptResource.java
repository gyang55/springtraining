package ca.emonster.training.ecommerce.web.rest;

import ca.emonster.training.ecommerce.repository.TblTaxExemptRepository;
import ca.emonster.training.ecommerce.service.TblTaxExemptQueryService;
import ca.emonster.training.ecommerce.service.TblTaxExemptService;
import ca.emonster.training.ecommerce.service.criteria.TblTaxExemptCriteria;
import ca.emonster.training.ecommerce.service.dto.TblTaxExemptDTO;
import ca.emonster.training.ecommerce.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link ca.emonster.training.ecommerce.domain.TblTaxExempt}.
 */
@RestController
@RequestMapping("/api")
public class TblTaxExemptResource {

    private final Logger log = LoggerFactory.getLogger(TblTaxExemptResource.class);

    private static final String ENTITY_NAME = "tblTaxExempt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TblTaxExemptService tblTaxExemptService;

    private final TblTaxExemptRepository tblTaxExemptRepository;

    private final TblTaxExemptQueryService tblTaxExemptQueryService;

    public TblTaxExemptResource(
        TblTaxExemptService tblTaxExemptService,
        TblTaxExemptRepository tblTaxExemptRepository,
        TblTaxExemptQueryService tblTaxExemptQueryService
    ) {
        this.tblTaxExemptService = tblTaxExemptService;
        this.tblTaxExemptRepository = tblTaxExemptRepository;
        this.tblTaxExemptQueryService = tblTaxExemptQueryService;
    }

    /**
     * {@code POST  /tbl-tax-exempts} : Create a new tblTaxExempt.
     *
     * @param tblTaxExemptDTO the tblTaxExemptDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tblTaxExemptDTO, or with status {@code 400 (Bad Request)} if the tblTaxExempt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tbl-tax-exempts")
    public ResponseEntity<TblTaxExemptDTO> createTblTaxExempt(@Valid @RequestBody TblTaxExemptDTO tblTaxExemptDTO)
        throws URISyntaxException {
        log.debug("REST request to save TblTaxExempt : {}", tblTaxExemptDTO);
        if (tblTaxExemptDTO.getId() != null) {
            throw new BadRequestAlertException("A new tblTaxExempt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TblTaxExemptDTO result = tblTaxExemptService.save(tblTaxExemptDTO);
        return ResponseEntity
            .created(new URI("/api/tbl-tax-exempts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tbl-tax-exempts/:id} : Updates an existing tblTaxExempt.
     *
     * @param id the id of the tblTaxExemptDTO to save.
     * @param tblTaxExemptDTO the tblTaxExemptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblTaxExemptDTO,
     * or with status {@code 400 (Bad Request)} if the tblTaxExemptDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tblTaxExemptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tbl-tax-exempts/{id}")
    public ResponseEntity<TblTaxExemptDTO> updateTblTaxExempt(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TblTaxExemptDTO tblTaxExemptDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TblTaxExempt : {}, {}", id, tblTaxExemptDTO);
        if (tblTaxExemptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblTaxExemptDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblTaxExemptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TblTaxExemptDTO result = tblTaxExemptService.save(tblTaxExemptDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblTaxExemptDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tbl-tax-exempts/:id} : Partial updates given fields of an existing tblTaxExempt, field will ignore if it is null
     *
     * @param id the id of the tblTaxExemptDTO to save.
     * @param tblTaxExemptDTO the tblTaxExemptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblTaxExemptDTO,
     * or with status {@code 400 (Bad Request)} if the tblTaxExemptDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tblTaxExemptDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tblTaxExemptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tbl-tax-exempts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TblTaxExemptDTO> partialUpdateTblTaxExempt(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TblTaxExemptDTO tblTaxExemptDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TblTaxExempt partially : {}, {}", id, tblTaxExemptDTO);
        if (tblTaxExemptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblTaxExemptDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblTaxExemptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TblTaxExemptDTO> result = tblTaxExemptService.partialUpdate(tblTaxExemptDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblTaxExemptDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tbl-tax-exempts} : get all the tblTaxExempts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tblTaxExempts in body.
     */
    @GetMapping("/tbl-tax-exempts")
    public ResponseEntity<List<TblTaxExemptDTO>> getAllTblTaxExempts(
        TblTaxExemptCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TblTaxExempts by criteria: {}", criteria);
        Page<TblTaxExemptDTO> page = tblTaxExemptQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tbl-tax-exempts/count} : count all the tblTaxExempts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tbl-tax-exempts/count")
    public ResponseEntity<Long> countTblTaxExempts(TblTaxExemptCriteria criteria) {
        log.debug("REST request to count TblTaxExempts by criteria: {}", criteria);
        return ResponseEntity.ok().body(tblTaxExemptQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tbl-tax-exempts/:id} : get the "id" tblTaxExempt.
     *
     * @param id the id of the tblTaxExemptDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tblTaxExemptDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tbl-tax-exempts/{id}")
    public ResponseEntity<TblTaxExemptDTO> getTblTaxExempt(@PathVariable Long id) {
        log.debug("REST request to get TblTaxExempt : {}", id);
        Optional<TblTaxExemptDTO> tblTaxExemptDTO = tblTaxExemptService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tblTaxExemptDTO);
    }

    /**
     * {@code DELETE  /tbl-tax-exempts/:id} : delete the "id" tblTaxExempt.
     *
     * @param id the id of the tblTaxExemptDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tbl-tax-exempts/{id}")
    public ResponseEntity<Void> deleteTblTaxExempt(@PathVariable Long id) {
        log.debug("REST request to delete TblTaxExempt : {}", id);
        tblTaxExemptService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
