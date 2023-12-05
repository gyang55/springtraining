package ca.emonster.training.ecommerce.web.rest;

import ca.emonster.training.ecommerce.repository.TblCustomerRepository;
import ca.emonster.training.ecommerce.service.TblCustomerQueryService;
import ca.emonster.training.ecommerce.service.TblCustomerService;
import ca.emonster.training.ecommerce.service.criteria.TblCustomerCriteria;
import ca.emonster.training.ecommerce.service.dto.TblCustomerDTO;
import ca.emonster.training.ecommerce.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link ca.emonster.training.ecommerce.domain.TblCustomer}.
 */
@RestController
@RequestMapping("/api")
public class TblCustomerResource {

    private final Logger log = LoggerFactory.getLogger(TblCustomerResource.class);

    private static final String ENTITY_NAME = "tblCustomer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TblCustomerService tblCustomerService;

    private final TblCustomerRepository tblCustomerRepository;

    private final TblCustomerQueryService tblCustomerQueryService;

    public TblCustomerResource(
        TblCustomerService tblCustomerService,
        TblCustomerRepository tblCustomerRepository,
        TblCustomerQueryService tblCustomerQueryService
    ) {
        this.tblCustomerService = tblCustomerService;
        this.tblCustomerRepository = tblCustomerRepository;
        this.tblCustomerQueryService = tblCustomerQueryService;
    }

    /**
     * {@code POST  /tbl-customers} : Create a new tblCustomer.
     *
     * @param tblCustomerDTO the tblCustomerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tblCustomerDTO, or with status {@code 400 (Bad Request)} if the tblCustomer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tbl-customers")
    public ResponseEntity<TblCustomerDTO> createTblCustomer(@RequestBody TblCustomerDTO tblCustomerDTO) throws URISyntaxException {
        log.debug("REST request to save TblCustomer : {}", tblCustomerDTO);
        if (tblCustomerDTO.getId() != null) {
            throw new BadRequestAlertException("A new tblCustomer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TblCustomerDTO result = tblCustomerService.save(tblCustomerDTO);
        return ResponseEntity
            .created(new URI("/api/tbl-customers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tbl-customers/:id} : Updates an existing tblCustomer.
     *
     * @param id the id of the tblCustomerDTO to save.
     * @param tblCustomerDTO the tblCustomerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblCustomerDTO,
     * or with status {@code 400 (Bad Request)} if the tblCustomerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tblCustomerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tbl-customers/{id}")
    public ResponseEntity<TblCustomerDTO> updateTblCustomer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblCustomerDTO tblCustomerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TblCustomer : {}, {}", id, tblCustomerDTO);
        if (tblCustomerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblCustomerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblCustomerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TblCustomerDTO result = tblCustomerService.save(tblCustomerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblCustomerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tbl-customers/:id} : Partial updates given fields of an existing tblCustomer, field will ignore if it is null
     *
     * @param id the id of the tblCustomerDTO to save.
     * @param tblCustomerDTO the tblCustomerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblCustomerDTO,
     * or with status {@code 400 (Bad Request)} if the tblCustomerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tblCustomerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tblCustomerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tbl-customers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TblCustomerDTO> partialUpdateTblCustomer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblCustomerDTO tblCustomerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TblCustomer partially : {}, {}", id, tblCustomerDTO);
        if (tblCustomerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblCustomerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblCustomerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TblCustomerDTO> result = tblCustomerService.partialUpdate(tblCustomerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblCustomerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tbl-customers} : get all the tblCustomers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tblCustomers in body.
     */
    @GetMapping("/tbl-customers")
    public ResponseEntity<List<TblCustomerDTO>> getAllTblCustomers(
        TblCustomerCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TblCustomers by criteria: {}", criteria);
        Page<TblCustomerDTO> page = tblCustomerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tbl-customers/count} : count all the tblCustomers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tbl-customers/count")
    public ResponseEntity<Long> countTblCustomers(TblCustomerCriteria criteria) {
        log.debug("REST request to count TblCustomers by criteria: {}", criteria);
        return ResponseEntity.ok().body(tblCustomerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tbl-customers/:id} : get the "id" tblCustomer.
     *
     * @param id the id of the tblCustomerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tblCustomerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tbl-customers/{id}")
    public ResponseEntity<TblCustomerDTO> getTblCustomer(@PathVariable Long id) {
        log.debug("REST request to get TblCustomer : {}", id);
        Optional<TblCustomerDTO> tblCustomerDTO = tblCustomerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tblCustomerDTO);
    }

    /**
     * {@code DELETE  /tbl-customers/:id} : delete the "id" tblCustomer.
     *
     * @param id the id of the tblCustomerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tbl-customers/{id}")
    public ResponseEntity<Void> deleteTblCustomer(@PathVariable Long id) {
        log.debug("REST request to delete TblCustomer : {}", id);
        tblCustomerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
