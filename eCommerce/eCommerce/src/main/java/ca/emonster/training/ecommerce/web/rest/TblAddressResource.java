package ca.emonster.training.ecommerce.web.rest;

import ca.emonster.training.ecommerce.repository.TblAddressRepository;
import ca.emonster.training.ecommerce.service.TblAddressQueryService;
import ca.emonster.training.ecommerce.service.TblAddressService;
import ca.emonster.training.ecommerce.service.criteria.TblAddressCriteria;
import ca.emonster.training.ecommerce.service.dto.TblAddressDTO;
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
 * REST controller for managing {@link ca.emonster.training.ecommerce.domain.TblAddress}.
 */
@RestController
@RequestMapping("/api")
public class TblAddressResource {

    private final Logger log = LoggerFactory.getLogger(TblAddressResource.class);

    private static final String ENTITY_NAME = "tblAddress";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TblAddressService tblAddressService;

    private final TblAddressRepository tblAddressRepository;

    private final TblAddressQueryService tblAddressQueryService;

    public TblAddressResource(
        TblAddressService tblAddressService,
        TblAddressRepository tblAddressRepository,
        TblAddressQueryService tblAddressQueryService
    ) {
        this.tblAddressService = tblAddressService;
        this.tblAddressRepository = tblAddressRepository;
        this.tblAddressQueryService = tblAddressQueryService;
    }

    /**
     * {@code POST  /tbl-addresses} : Create a new tblAddress.
     *
     * @param tblAddressDTO the tblAddressDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tblAddressDTO, or with status {@code 400 (Bad Request)} if the tblAddress has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tbl-addresses")
    public ResponseEntity<TblAddressDTO> createTblAddress(@Valid @RequestBody TblAddressDTO tblAddressDTO) throws URISyntaxException {
        log.debug("REST request to save TblAddress : {}", tblAddressDTO);
        if (tblAddressDTO.getId() != null) {
            throw new BadRequestAlertException("A new tblAddress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TblAddressDTO result = tblAddressService.save(tblAddressDTO);
        return ResponseEntity
            .created(new URI("/api/tbl-addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tbl-addresses/:id} : Updates an existing tblAddress.
     *
     * @param id the id of the tblAddressDTO to save.
     * @param tblAddressDTO the tblAddressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblAddressDTO,
     * or with status {@code 400 (Bad Request)} if the tblAddressDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tblAddressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tbl-addresses/{id}")
    public ResponseEntity<TblAddressDTO> updateTblAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TblAddressDTO tblAddressDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TblAddress : {}, {}", id, tblAddressDTO);
        if (tblAddressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblAddressDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TblAddressDTO result = tblAddressService.save(tblAddressDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblAddressDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tbl-addresses/:id} : Partial updates given fields of an existing tblAddress, field will ignore if it is null
     *
     * @param id the id of the tblAddressDTO to save.
     * @param tblAddressDTO the tblAddressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblAddressDTO,
     * or with status {@code 400 (Bad Request)} if the tblAddressDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tblAddressDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tblAddressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tbl-addresses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TblAddressDTO> partialUpdateTblAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TblAddressDTO tblAddressDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TblAddress partially : {}, {}", id, tblAddressDTO);
        if (tblAddressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblAddressDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TblAddressDTO> result = tblAddressService.partialUpdate(tblAddressDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblAddressDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tbl-addresses} : get all the tblAddresses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tblAddresses in body.
     */
    @GetMapping("/tbl-addresses")
    public ResponseEntity<List<TblAddressDTO>> getAllTblAddresses(
        TblAddressCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TblAddresses by criteria: {}", criteria);
        Page<TblAddressDTO> page = tblAddressQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tbl-addresses/count} : count all the tblAddresses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tbl-addresses/count")
    public ResponseEntity<Long> countTblAddresses(TblAddressCriteria criteria) {
        log.debug("REST request to count TblAddresses by criteria: {}", criteria);
        return ResponseEntity.ok().body(tblAddressQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tbl-addresses/:id} : get the "id" tblAddress.
     *
     * @param id the id of the tblAddressDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tblAddressDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tbl-addresses/{id}")
    public ResponseEntity<TblAddressDTO> getTblAddress(@PathVariable Long id) {
        log.debug("REST request to get TblAddress : {}", id);
        Optional<TblAddressDTO> tblAddressDTO = tblAddressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tblAddressDTO);
    }

    /**
     * {@code DELETE  /tbl-addresses/:id} : delete the "id" tblAddress.
     *
     * @param id the id of the tblAddressDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tbl-addresses/{id}")
    public ResponseEntity<Void> deleteTblAddress(@PathVariable Long id) {
        log.debug("REST request to delete TblAddress : {}", id);
        tblAddressService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
