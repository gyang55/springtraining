package ca.emonster.training.ecommerce.web.rest;

import ca.emonster.training.ecommerce.repository.TblContactRepository;
import ca.emonster.training.ecommerce.service.TblContactQueryService;
import ca.emonster.training.ecommerce.service.TblContactService;
import ca.emonster.training.ecommerce.service.criteria.TblContactCriteria;
import ca.emonster.training.ecommerce.service.dto.TblContactDTO;
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
 * REST controller for managing {@link ca.emonster.training.ecommerce.domain.TblContact}.
 */
@RestController
@RequestMapping("/api")
public class TblContactResource {

    private final Logger log = LoggerFactory.getLogger(TblContactResource.class);

    private static final String ENTITY_NAME = "tblContact";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TblContactService tblContactService;

    private final TblContactRepository tblContactRepository;

    private final TblContactQueryService tblContactQueryService;

    public TblContactResource(
        TblContactService tblContactService,
        TblContactRepository tblContactRepository,
        TblContactQueryService tblContactQueryService
    ) {
        this.tblContactService = tblContactService;
        this.tblContactRepository = tblContactRepository;
        this.tblContactQueryService = tblContactQueryService;
    }

    /**
     * {@code POST  /tbl-contacts} : Create a new tblContact.
     *
     * @param tblContactDTO the tblContactDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tblContactDTO, or with status {@code 400 (Bad Request)} if the tblContact has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tbl-contacts")
    public ResponseEntity<TblContactDTO> createTblContact(@Valid @RequestBody TblContactDTO tblContactDTO) throws URISyntaxException {
        log.debug("REST request to save TblContact : {}", tblContactDTO);
        if (tblContactDTO.getId() != null) {
            throw new BadRequestAlertException("A new tblContact cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TblContactDTO result = tblContactService.save(tblContactDTO);
        return ResponseEntity
            .created(new URI("/api/tbl-contacts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tbl-contacts/:id} : Updates an existing tblContact.
     *
     * @param id the id of the tblContactDTO to save.
     * @param tblContactDTO the tblContactDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblContactDTO,
     * or with status {@code 400 (Bad Request)} if the tblContactDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tblContactDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tbl-contacts/{id}")
    public ResponseEntity<TblContactDTO> updateTblContact(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TblContactDTO tblContactDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TblContact : {}, {}", id, tblContactDTO);
        if (tblContactDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblContactDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblContactRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TblContactDTO result = tblContactService.save(tblContactDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblContactDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tbl-contacts/:id} : Partial updates given fields of an existing tblContact, field will ignore if it is null
     *
     * @param id the id of the tblContactDTO to save.
     * @param tblContactDTO the tblContactDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblContactDTO,
     * or with status {@code 400 (Bad Request)} if the tblContactDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tblContactDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tblContactDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tbl-contacts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TblContactDTO> partialUpdateTblContact(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TblContactDTO tblContactDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TblContact partially : {}, {}", id, tblContactDTO);
        if (tblContactDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblContactDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblContactRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TblContactDTO> result = tblContactService.partialUpdate(tblContactDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblContactDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tbl-contacts} : get all the tblContacts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tblContacts in body.
     */
    @GetMapping("/tbl-contacts")
    public ResponseEntity<List<TblContactDTO>> getAllTblContacts(
        TblContactCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TblContacts by criteria: {}", criteria);
        Page<TblContactDTO> page = tblContactQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tbl-contacts/count} : count all the tblContacts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tbl-contacts/count")
    public ResponseEntity<Long> countTblContacts(TblContactCriteria criteria) {
        log.debug("REST request to count TblContacts by criteria: {}", criteria);
        return ResponseEntity.ok().body(tblContactQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tbl-contacts/:id} : get the "id" tblContact.
     *
     * @param id the id of the tblContactDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tblContactDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tbl-contacts/{id}")
    public ResponseEntity<TblContactDTO> getTblContact(@PathVariable Long id) {
        log.debug("REST request to get TblContact : {}", id);
        Optional<TblContactDTO> tblContactDTO = tblContactService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tblContactDTO);
    }

    /**
     * {@code DELETE  /tbl-contacts/:id} : delete the "id" tblContact.
     *
     * @param id the id of the tblContactDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tbl-contacts/{id}")
    public ResponseEntity<Void> deleteTblContact(@PathVariable Long id) {
        log.debug("REST request to delete TblContact : {}", id);
        tblContactService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
