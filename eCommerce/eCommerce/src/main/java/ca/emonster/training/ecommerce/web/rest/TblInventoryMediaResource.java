package ca.emonster.training.ecommerce.web.rest;

import ca.emonster.training.ecommerce.repository.TblInventoryMediaRepository;
import ca.emonster.training.ecommerce.service.TblInventoryMediaQueryService;
import ca.emonster.training.ecommerce.service.TblInventoryMediaService;
import ca.emonster.training.ecommerce.service.criteria.TblInventoryMediaCriteria;
import ca.emonster.training.ecommerce.service.dto.TblInventoryMediaDTO;
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
 * REST controller for managing {@link ca.emonster.training.ecommerce.domain.TblInventoryMedia}.
 */
@RestController
@RequestMapping("/api")
public class TblInventoryMediaResource {

    private final Logger log = LoggerFactory.getLogger(TblInventoryMediaResource.class);

    private static final String ENTITY_NAME = "tblInventoryMedia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TblInventoryMediaService tblInventoryMediaService;

    private final TblInventoryMediaRepository tblInventoryMediaRepository;

    private final TblInventoryMediaQueryService tblInventoryMediaQueryService;

    public TblInventoryMediaResource(
        TblInventoryMediaService tblInventoryMediaService,
        TblInventoryMediaRepository tblInventoryMediaRepository,
        TblInventoryMediaQueryService tblInventoryMediaQueryService
    ) {
        this.tblInventoryMediaService = tblInventoryMediaService;
        this.tblInventoryMediaRepository = tblInventoryMediaRepository;
        this.tblInventoryMediaQueryService = tblInventoryMediaQueryService;
    }

    /**
     * {@code POST  /tbl-inventory-medias} : Create a new tblInventoryMedia.
     *
     * @param tblInventoryMediaDTO the tblInventoryMediaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tblInventoryMediaDTO, or with status {@code 400 (Bad Request)} if the tblInventoryMedia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tbl-inventory-medias")
    public ResponseEntity<TblInventoryMediaDTO> createTblInventoryMedia(@RequestBody TblInventoryMediaDTO tblInventoryMediaDTO)
        throws URISyntaxException {
        log.debug("REST request to save TblInventoryMedia : {}", tblInventoryMediaDTO);
        if (tblInventoryMediaDTO.getId() != null) {
            throw new BadRequestAlertException("A new tblInventoryMedia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TblInventoryMediaDTO result = tblInventoryMediaService.save(tblInventoryMediaDTO);
        return ResponseEntity
            .created(new URI("/api/tbl-inventory-medias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tbl-inventory-medias/:id} : Updates an existing tblInventoryMedia.
     *
     * @param id the id of the tblInventoryMediaDTO to save.
     * @param tblInventoryMediaDTO the tblInventoryMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblInventoryMediaDTO,
     * or with status {@code 400 (Bad Request)} if the tblInventoryMediaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tblInventoryMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tbl-inventory-medias/{id}")
    public ResponseEntity<TblInventoryMediaDTO> updateTblInventoryMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblInventoryMediaDTO tblInventoryMediaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TblInventoryMedia : {}, {}", id, tblInventoryMediaDTO);
        if (tblInventoryMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblInventoryMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblInventoryMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TblInventoryMediaDTO result = tblInventoryMediaService.save(tblInventoryMediaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblInventoryMediaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tbl-inventory-medias/:id} : Partial updates given fields of an existing tblInventoryMedia, field will ignore if it is null
     *
     * @param id the id of the tblInventoryMediaDTO to save.
     * @param tblInventoryMediaDTO the tblInventoryMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblInventoryMediaDTO,
     * or with status {@code 400 (Bad Request)} if the tblInventoryMediaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tblInventoryMediaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tblInventoryMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tbl-inventory-medias/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TblInventoryMediaDTO> partialUpdateTblInventoryMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblInventoryMediaDTO tblInventoryMediaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TblInventoryMedia partially : {}, {}", id, tblInventoryMediaDTO);
        if (tblInventoryMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblInventoryMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblInventoryMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TblInventoryMediaDTO> result = tblInventoryMediaService.partialUpdate(tblInventoryMediaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblInventoryMediaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tbl-inventory-medias} : get all the tblInventoryMedias.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tblInventoryMedias in body.
     */
    @GetMapping("/tbl-inventory-medias")
    public ResponseEntity<List<TblInventoryMediaDTO>> getAllTblInventoryMedias(
        TblInventoryMediaCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TblInventoryMedias by criteria: {}", criteria);
        Page<TblInventoryMediaDTO> page = tblInventoryMediaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tbl-inventory-medias/count} : count all the tblInventoryMedias.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tbl-inventory-medias/count")
    public ResponseEntity<Long> countTblInventoryMedias(TblInventoryMediaCriteria criteria) {
        log.debug("REST request to count TblInventoryMedias by criteria: {}", criteria);
        return ResponseEntity.ok().body(tblInventoryMediaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tbl-inventory-medias/:id} : get the "id" tblInventoryMedia.
     *
     * @param id the id of the tblInventoryMediaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tblInventoryMediaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tbl-inventory-medias/{id}")
    public ResponseEntity<TblInventoryMediaDTO> getTblInventoryMedia(@PathVariable Long id) {
        log.debug("REST request to get TblInventoryMedia : {}", id);
        Optional<TblInventoryMediaDTO> tblInventoryMediaDTO = tblInventoryMediaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tblInventoryMediaDTO);
    }

    /**
     * {@code DELETE  /tbl-inventory-medias/:id} : delete the "id" tblInventoryMedia.
     *
     * @param id the id of the tblInventoryMediaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tbl-inventory-medias/{id}")
    public ResponseEntity<Void> deleteTblInventoryMedia(@PathVariable Long id) {
        log.debug("REST request to delete TblInventoryMedia : {}", id);
        tblInventoryMediaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
