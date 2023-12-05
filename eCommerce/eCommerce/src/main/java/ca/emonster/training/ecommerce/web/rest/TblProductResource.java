package ca.emonster.training.ecommerce.web.rest;

import ca.emonster.training.ecommerce.repository.TblProductRepository;
import ca.emonster.training.ecommerce.service.TblProductQueryService;
import ca.emonster.training.ecommerce.service.TblProductService;
import ca.emonster.training.ecommerce.service.criteria.TblProductCriteria;
import ca.emonster.training.ecommerce.service.dto.TblProductDTO;
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
 * REST controller for managing {@link ca.emonster.training.ecommerce.domain.TblProduct}.
 */
@RestController
@RequestMapping("/api")
public class TblProductResource {

    private final Logger log = LoggerFactory.getLogger(TblProductResource.class);

    private static final String ENTITY_NAME = "tblProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TblProductService tblProductService;

    private final TblProductRepository tblProductRepository;

    private final TblProductQueryService tblProductQueryService;

    public TblProductResource(
        TblProductService tblProductService,
        TblProductRepository tblProductRepository,
        TblProductQueryService tblProductQueryService
    ) {
        this.tblProductService = tblProductService;
        this.tblProductRepository = tblProductRepository;
        this.tblProductQueryService = tblProductQueryService;
    }

    /**
     * {@code POST  /tbl-products} : Create a new tblProduct.
     *
     * @param tblProductDTO the tblProductDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tblProductDTO, or with status {@code 400 (Bad Request)} if the tblProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tbl-products")
    public ResponseEntity<TblProductDTO> createTblProduct(@RequestBody TblProductDTO tblProductDTO) throws URISyntaxException {
        log.debug("REST request to save TblProduct : {}", tblProductDTO);
        if (tblProductDTO.getId() != null) {
            throw new BadRequestAlertException("A new tblProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TblProductDTO result = tblProductService.save(tblProductDTO);
        return ResponseEntity
            .created(new URI("/api/tbl-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tbl-products/:id} : Updates an existing tblProduct.
     *
     * @param id the id of the tblProductDTO to save.
     * @param tblProductDTO the tblProductDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblProductDTO,
     * or with status {@code 400 (Bad Request)} if the tblProductDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tblProductDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tbl-products/{id}")
    public ResponseEntity<TblProductDTO> updateTblProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblProductDTO tblProductDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TblProduct : {}, {}", id, tblProductDTO);
        if (tblProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblProductDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TblProductDTO result = tblProductService.save(tblProductDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblProductDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tbl-products/:id} : Partial updates given fields of an existing tblProduct, field will ignore if it is null
     *
     * @param id the id of the tblProductDTO to save.
     * @param tblProductDTO the tblProductDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblProductDTO,
     * or with status {@code 400 (Bad Request)} if the tblProductDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tblProductDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tblProductDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tbl-products/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TblProductDTO> partialUpdateTblProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblProductDTO tblProductDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TblProduct partially : {}, {}", id, tblProductDTO);
        if (tblProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblProductDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TblProductDTO> result = tblProductService.partialUpdate(tblProductDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblProductDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tbl-products} : get all the tblProducts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tblProducts in body.
     */
    @GetMapping("/tbl-products")
    public ResponseEntity<List<TblProductDTO>> getAllTblProducts(
        TblProductCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TblProducts by criteria: {}", criteria);
        Page<TblProductDTO> page = tblProductQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tbl-products/count} : count all the tblProducts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tbl-products/count")
    public ResponseEntity<Long> countTblProducts(TblProductCriteria criteria) {
        log.debug("REST request to count TblProducts by criteria: {}", criteria);
        return ResponseEntity.ok().body(tblProductQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tbl-products/:id} : get the "id" tblProduct.
     *
     * @param id the id of the tblProductDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tblProductDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tbl-products/{id}")
    public ResponseEntity<TblProductDTO> getTblProduct(@PathVariable Long id) {
        log.debug("REST request to get TblProduct : {}", id);
        Optional<TblProductDTO> tblProductDTO = tblProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tblProductDTO);
    }

    /**
     * {@code DELETE  /tbl-products/:id} : delete the "id" tblProduct.
     *
     * @param id the id of the tblProductDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tbl-products/{id}")
    public ResponseEntity<Void> deleteTblProduct(@PathVariable Long id) {
        log.debug("REST request to delete TblProduct : {}", id);
        tblProductService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
