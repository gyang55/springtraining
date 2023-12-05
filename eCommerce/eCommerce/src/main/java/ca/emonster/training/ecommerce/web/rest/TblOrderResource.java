package ca.emonster.training.ecommerce.web.rest;

import ca.emonster.training.ecommerce.repository.TblOrderRepository;
import ca.emonster.training.ecommerce.service.TblOrderQueryService;
import ca.emonster.training.ecommerce.service.TblOrderService;
import ca.emonster.training.ecommerce.service.criteria.TblOrderCriteria;
import ca.emonster.training.ecommerce.service.dto.TblOrderDTO;
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
 * REST controller for managing {@link ca.emonster.training.ecommerce.domain.TblOrder}.
 */
@RestController
@RequestMapping("/api")
public class TblOrderResource {

    private final Logger log = LoggerFactory.getLogger(TblOrderResource.class);

    private static final String ENTITY_NAME = "tblOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TblOrderService tblOrderService;

    private final TblOrderRepository tblOrderRepository;

    private final TblOrderQueryService tblOrderQueryService;

    public TblOrderResource(
        TblOrderService tblOrderService,
        TblOrderRepository tblOrderRepository,
        TblOrderQueryService tblOrderQueryService
    ) {
        this.tblOrderService = tblOrderService;
        this.tblOrderRepository = tblOrderRepository;
        this.tblOrderQueryService = tblOrderQueryService;
    }

    /**
     * {@code POST  /tbl-orders} : Create a new tblOrder.
     *
     * @param tblOrderDTO the tblOrderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tblOrderDTO, or with status {@code 400 (Bad Request)} if the tblOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tbl-orders")
    public ResponseEntity<TblOrderDTO> createTblOrder(@RequestBody TblOrderDTO tblOrderDTO) throws URISyntaxException {
        log.debug("REST request to save TblOrder : {}", tblOrderDTO);
        if (tblOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new tblOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TblOrderDTO result = tblOrderService.save(tblOrderDTO);
        return ResponseEntity
            .created(new URI("/api/tbl-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tbl-orders/:id} : Updates an existing tblOrder.
     *
     * @param id the id of the tblOrderDTO to save.
     * @param tblOrderDTO the tblOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblOrderDTO,
     * or with status {@code 400 (Bad Request)} if the tblOrderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tblOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tbl-orders/{id}")
    public ResponseEntity<TblOrderDTO> updateTblOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblOrderDTO tblOrderDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TblOrder : {}, {}", id, tblOrderDTO);
        if (tblOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TblOrderDTO result = tblOrderService.save(tblOrderDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblOrderDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tbl-orders/:id} : Partial updates given fields of an existing tblOrder, field will ignore if it is null
     *
     * @param id the id of the tblOrderDTO to save.
     * @param tblOrderDTO the tblOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tblOrderDTO,
     * or with status {@code 400 (Bad Request)} if the tblOrderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tblOrderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tblOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tbl-orders/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TblOrderDTO> partialUpdateTblOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TblOrderDTO tblOrderDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TblOrder partially : {}, {}", id, tblOrderDTO);
        if (tblOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tblOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tblOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TblOrderDTO> result = tblOrderService.partialUpdate(tblOrderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tblOrderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tbl-orders} : get all the tblOrders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tblOrders in body.
     */
    @GetMapping("/tbl-orders")
    public ResponseEntity<List<TblOrderDTO>> getAllTblOrders(
        TblOrderCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TblOrders by criteria: {}", criteria);
        Page<TblOrderDTO> page = tblOrderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tbl-orders/count} : count all the tblOrders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tbl-orders/count")
    public ResponseEntity<Long> countTblOrders(TblOrderCriteria criteria) {
        log.debug("REST request to count TblOrders by criteria: {}", criteria);
        return ResponseEntity.ok().body(tblOrderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tbl-orders/:id} : get the "id" tblOrder.
     *
     * @param id the id of the tblOrderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tblOrderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tbl-orders/{id}")
    public ResponseEntity<TblOrderDTO> getTblOrder(@PathVariable Long id) {
        log.debug("REST request to get TblOrder : {}", id);
        Optional<TblOrderDTO> tblOrderDTO = tblOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tblOrderDTO);
    }

    /**
     * {@code DELETE  /tbl-orders/:id} : delete the "id" tblOrder.
     *
     * @param id the id of the tblOrderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tbl-orders/{id}")
    public ResponseEntity<Void> deleteTblOrder(@PathVariable Long id) {
        log.debug("REST request to delete TblOrder : {}", id);
        tblOrderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
