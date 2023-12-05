package ca.emonster.training.ecommerce.service;

import ca.emonster.training.ecommerce.domain.*; // for static metamodels
import ca.emonster.training.ecommerce.domain.TblOrder;
import ca.emonster.training.ecommerce.repository.TblOrderRepository;
import ca.emonster.training.ecommerce.service.criteria.TblOrderCriteria;
import ca.emonster.training.ecommerce.service.dto.TblOrderDTO;
import ca.emonster.training.ecommerce.service.mapper.TblOrderMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TblOrder} entities in the database.
 * The main input is a {@link TblOrderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TblOrderDTO} or a {@link Page} of {@link TblOrderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TblOrderQueryService extends QueryService<TblOrder> {

    private final Logger log = LoggerFactory.getLogger(TblOrderQueryService.class);

    private final TblOrderRepository tblOrderRepository;

    private final TblOrderMapper tblOrderMapper;

    public TblOrderQueryService(TblOrderRepository tblOrderRepository, TblOrderMapper tblOrderMapper) {
        this.tblOrderRepository = tblOrderRepository;
        this.tblOrderMapper = tblOrderMapper;
    }

    /**
     * Return a {@link List} of {@link TblOrderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TblOrderDTO> findByCriteria(TblOrderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TblOrder> specification = createSpecification(criteria);
        return tblOrderMapper.toDto(tblOrderRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TblOrderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TblOrderDTO> findByCriteria(TblOrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TblOrder> specification = createSpecification(criteria);
        return tblOrderRepository.findAll(specification, page).map(tblOrderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TblOrderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TblOrder> specification = createSpecification(criteria);
        return tblOrderRepository.count(specification);
    }

    /**
     * Function to convert {@link TblOrderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TblOrder> createSpecification(TblOrderCriteria criteria) {
        Specification<TblOrder> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TblOrder_.id));
            }
            if (criteria.getOrderNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrderNumber(), TblOrder_.orderNumber));
            }
            if (criteria.getOrderDate() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrderDate(), TblOrder_.orderDate));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), TblOrder_.status));
            }
            if (criteria.getPaymentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPaymentId(), root -> root.join(TblOrder_.payment, JoinType.LEFT).get(TblPayment_.id))
                    );
            }
            if (criteria.getShipToId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getShipToId(), root -> root.join(TblOrder_.shipTo, JoinType.LEFT).get(TblContact_.id))
                    );
            }
            if (criteria.getItemsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getItemsId(), root -> root.join(TblOrder_.items, JoinType.LEFT).get(TblItem_.id))
                    );
            }
            if (criteria.getCustomerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerId(),
                            root -> root.join(TblOrder_.customer, JoinType.LEFT).get(TblCustomer_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
