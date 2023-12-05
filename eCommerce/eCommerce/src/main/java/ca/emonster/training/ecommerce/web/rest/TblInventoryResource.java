package ca.emonster.training.ecommerce.web.rest;

import ca.emonster.training.ecommerce.repository.TblInventoryRepository;
import ca.emonster.training.ecommerce.service.TblInventoryQueryService;
import ca.emonster.training.ecommerce.service.TblInventoryService;
import ca.emonster.training.ecommerce.service.criteria.TblInventoryCriteria;
import ca.emonster.training.ecommerce.service.dto.TblInventoryDTO;
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
 * REST controller for managing {@link ca.emonster.training.ecommerce.domain.TblInventory}.
 */
@RestController
@RequestMapping("/api")
public class TblInventoryResource {

    private final Logger log = LoggerFactory.getLogger(TblInventoryResource.class);

    private static final String ENTITY_NAME = "tblInventory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TblInventoryService tblInventoryService;

    private final TblInventoryRepository tblInventoryRepository;

    private final TblInventoryQueryService tblInventoryQueryService;

    public TblInventoryResource(
        TblInventoryService tblInventoryService,
        TblInventoryRepository tblInventoryRepository,
        TblInventoryQueryService tblInventoryQueryService
    ) {
        this.tblInventoryService = tblInventoryService;
        this.tblInventoryRepository = tblInventoryRepository;
        this.tblInventoryQueryService = tblInventoryQueryService;
    }

    /**
     * {@code POST  /tbl-inventories} : Create a new tblInventory.
     *
     * @param tblInventoryDTO the tblInventoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tblInventoryDTO, or with status {@code 400 (Bad Request)} if the tblInventory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tbl-inventories")
    public ResponseEntity<TblInventoryDTO> createTblInventory(@Valid @RequestBody TblInventoryDTO tblInventoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save TblInventory : {}", tblInventoryDTO);
        if (tblInventoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new tblInventory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TblInventoryDTO result = tblInventoryService.save(tblInventoryDTO);
        return ResponseEntity
            .created(new URI("/api/tbl-inventories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tbl-inventories/:id} : Updates an existing tblInventory.
     *
     * @param id the id of the tblInventoryDTO to save.
     * @param tblInventoryDTO the tblInventoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblInventoryDTO,
     * or with status {@code 400 (Bad Request)} if the tblInventoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tblInventoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tbl-inventories/{id}")
    public ResponseEntity<TblInventoryDTO> updateTblInventory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TblInventoryDTO tblInventoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TblInventory : {}, {}", id, tblInventoryDTO);
        if (tblInventoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblInventoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblInventoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TblInventoryDTO result = tblInventoryService.save(tblInventoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblInventoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tbl-inventories/:id} : Partial updates given fields of an existing tblInventory, field will ignore if it is null
     *
     * @param id the id of the tblInventoryDTO to save.
     * @param tblInventoryDTO the tblInventoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblInventoryDTO,
     * or with status {@code 400 (Bad Request)} if the tblInventoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tblInventoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tblInventoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tbl-inventories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TblInventoryDTO> partialUpdateTblInventory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TblInventoryDTO tblInventoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TblInventory partially : {}, {}", id, tblInventoryDTO);
        if (tblInventoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblInventoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblInventoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TblInventoryDTO> result = tblInventoryService.partialUpdate(tblInventoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblInventoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tbl-inventories} : get all the tblInventories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tblInventories in body.
     */
    @GetMapping("/tbl-inventories")
    public ResponseEntity<List<TblInventoryDTO>> getAllTblInventories(
        TblInventoryCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TblInventories by criteria: {}", criteria);
        Page<TblInventoryDTO> page = tblInventoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tbl-inventories/count} : count all the tblInventories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tbl-inventories/count")
    public ResponseEntity<Long> countTblInventories(TblInventoryCriteria criteria) {
        log.debug("REST request to count TblInventories by criteria: {}", criteria);
        return ResponseEntity.ok().body(tblInventoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tbl-inventories/:id} : get the "id" tblInventory.
     *
     * @param id the id of the tblInventoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tblInventoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tbl-inventories/{id}")
    public ResponseEntity<TblInventoryDTO> getTblInventory(@PathVariable Long id) {
        log.debug("REST request to get TblInventory : {}", id);
        Optional<TblInventoryDTO> tblInventoryDTO = tblInventoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tblInventoryDTO);
    }

    /**
     * {@code DELETE  /tbl-inventories/:id} : delete the "id" tblInventory.
     *
     * @param id the id of the tblInventoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tbl-inventories/{id}")
    public ResponseEntity<Void> deleteTblInventory(@PathVariable Long id) {
        log.debug("REST request to delete TblInventory : {}", id);
        tblInventoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
