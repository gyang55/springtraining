package ca.emonster.training.ecommerce.web.rest;

import ca.emonster.training.ecommerce.repository.TblItemRepository;
import ca.emonster.training.ecommerce.service.TblItemQueryService;
import ca.emonster.training.ecommerce.service.TblItemService;
import ca.emonster.training.ecommerce.service.criteria.TblItemCriteria;
import ca.emonster.training.ecommerce.service.dto.TblItemDTO;
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
 * REST controller for managing {@link ca.emonster.training.ecommerce.domain.TblItem}.
 */
@RestController
@RequestMapping("/api")
public class TblItemResource {

    private final Logger log = LoggerFactory.getLogger(TblItemResource.class);

    private static final String ENTITY_NAME = "tblItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TblItemService tblItemService;

    private final TblItemRepository tblItemRepository;

    private final TblItemQueryService tblItemQueryService;

    public TblItemResource(TblItemService tblItemService, TblItemRepository tblItemRepository, TblItemQueryService tblItemQueryService) {
        this.tblItemService = tblItemService;
        this.tblItemRepository = tblItemRepository;
        this.tblItemQueryService = tblItemQueryService;
    }

    /**
     * {@code POST  /tbl-items} : Create a new tblItem.
     *
     * @param tblItemDTO the tblItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tblItemDTO, or with status {@code 400 (Bad Request)} if the tblItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tbl-items")
    public ResponseEntity<TblItemDTO> createTblItem(@RequestBody TblItemDTO tblItemDTO) throws URISyntaxException {
        log.debug("REST request to save TblItem : {}", tblItemDTO);
        if (tblItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new tblItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TblItemDTO result = tblItemService.save(tblItemDTO);
        return ResponseEntity
            .created(new URI("/api/tbl-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tbl-items/:id} : Updates an existing tblItem.
     *
     * @param id the id of the tblItemDTO to save.
     * @param tblItemDTO the tblItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblItemDTO,
     * or with status {@code 400 (Bad Request)} if the tblItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tblItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tbl-items/{id}")
    public ResponseEntity<TblItemDTO> updateTblItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblItemDTO tblItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TblItem : {}, {}", id, tblItemDTO);
        if (tblItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TblItemDTO result = tblItemService.save(tblItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tbl-items/:id} : Partial updates given fields of an existing tblItem, field will ignore if it is null
     *
     * @param id the id of the tblItemDTO to save.
     * @param tblItemDTO the tblItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblItemDTO,
     * or with status {@code 400 (Bad Request)} if the tblItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tblItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tblItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tbl-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TblItemDTO> partialUpdateTblItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblItemDTO tblItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TblItem partially : {}, {}", id, tblItemDTO);
        if (tblItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TblItemDTO> result = tblItemService.partialUpdate(tblItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tbl-items} : get all the tblItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tblItems in body.
     */
    @GetMapping("/tbl-items")
    public ResponseEntity<List<TblItemDTO>> getAllTblItems(
        TblItemCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TblItems by criteria: {}", criteria);
        Page<TblItemDTO> page = tblItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tbl-items/count} : count all the tblItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tbl-items/count")
    public ResponseEntity<Long> countTblItems(TblItemCriteria criteria) {
        log.debug("REST request to count TblItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(tblItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tbl-items/:id} : get the "id" tblItem.
     *
     * @param id the id of the tblItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tblItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tbl-items/{id}")
    public ResponseEntity<TblItemDTO> getTblItem(@PathVariable Long id) {
        log.debug("REST request to get TblItem : {}", id);
        Optional<TblItemDTO> tblItemDTO = tblItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tblItemDTO);
    }

    /**
     * {@code DELETE  /tbl-items/:id} : delete the "id" tblItem.
     *
     * @param id the id of the tblItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tbl-items/{id}")
    public ResponseEntity<Void> deleteTblItem(@PathVariable Long id) {
        log.debug("REST request to delete TblItem : {}", id);
        tblItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
