package ca.emonster.training.ecommerce.service;

import ca.emonster.training.ecommerce.domain.*; // for static metamodels
import ca.emonster.training.ecommerce.domain.TblPayment;
import ca.emonster.training.ecommerce.repository.TblPaymentRepository;
import ca.emonster.training.ecommerce.service.criteria.TblPaymentCriteria;
import ca.emonster.training.ecommerce.service.dto.TblPaymentDTO;
import ca.emonster.training.ecommerce.service.mapper.TblPaymentMapper;
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
 * Service for executing complex queries for {@link TblPayment} entities in the database.
 * The main input is a {@link TblPaymentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TblPaymentDTO} or a {@link Page} of {@link TblPaymentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TblPaymentQueryService extends QueryService<TblPayment> {

    private final Logger log = LoggerFactory.getLogger(TblPaymentQueryService.class);

    private final TblPaymentRepository tblPaymentRepository;

    private final TblPaymentMapper tblPaymentMapper;

    public TblPaymentQueryService(TblPaymentRepository tblPaymentRepository, TblPaymentMapper tblPaymentMapper) {
        this.tblPaymentRepository = tblPaymentRepository;
        this.tblPaymentMapper = tblPaymentMapper;
    }

    /**
     * Return a {@link List} of {@link TblPaymentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TblPaymentDTO> findByCriteria(TblPaymentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TblPayment> specification = createSpecification(criteria);
        return tblPaymentMapper.toDto(tblPaymentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TblPaymentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TblPaymentDTO> findByCriteria(TblPaymentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TblPayment> specification = createSpecification(criteria);
        return tblPaymentRepository.findAll(specification, page).map(tblPaymentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TblPaymentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TblPayment> specification = createSpecification(criteria);
        return tblPaymentRepository.count(specification);
    }

    /**
     * Function to convert {@link TblPaymentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TblPayment> createSpecification(TblPaymentCriteria criteria) {
        Specification<TblPayment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TblPayment_.id));
            }
            if (criteria.getChannel() != null) {
                specification = specification.and(buildSpecification(criteria.getChannel(), TblPayment_.channel));
            }
            if (criteria.getMethod() != null) {
                specification = specification.and(buildSpecification(criteria.getMethod(), TblPayment_.method));
            }
            if (criteria.getOrderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOrderId(), root -> root.join(TblPayment_.order, JoinType.LEFT).get(TblOrder_.id))
                    );
            }
        }
        return specification;
    }
}
