package ca.emonster.training.ecommerce.service;

import ca.emonster.training.ecommerce.domain.*; // for static metamodels
import ca.emonster.training.ecommerce.domain.TblCustomer;
import ca.emonster.training.ecommerce.repository.TblCustomerRepository;
import ca.emonster.training.ecommerce.service.criteria.TblCustomerCriteria;
import ca.emonster.training.ecommerce.service.dto.TblCustomerDTO;
import ca.emonster.training.ecommerce.service.mapper.TblCustomerMapper;
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
 * Service for executing complex queries for {@link TblCustomer} entities in the database.
 * The main input is a {@link TblCustomerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TblCustomerDTO} or a {@link Page} of {@link TblCustomerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TblCustomerQueryService extends QueryService<TblCustomer> {

    private final Logger log = LoggerFactory.getLogger(TblCustomerQueryService.class);

    private final TblCustomerRepository tblCustomerRepository;

    private final TblCustomerMapper tblCustomerMapper;

    public TblCustomerQueryService(TblCustomerRepository tblCustomerRepository, TblCustomerMapper tblCustomerMapper) {
        this.tblCustomerRepository = tblCustomerRepository;
        this.tblCustomerMapper = tblCustomerMapper;
    }

    /**
     * Return a {@link List} of {@link TblCustomerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TblCustomerDTO> findByCriteria(TblCustomerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TblCustomer> specification = createSpecification(criteria);
        return tblCustomerMapper.toDto(tblCustomerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TblCustomerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TblCustomerDTO> findByCriteria(TblCustomerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TblCustomer> specification = createSpecification(criteria);
        return tblCustomerRepository.findAll(specification, page).map(tblCustomerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TblCustomerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TblCustomer> specification = createSpecification(criteria);
        return tblCustomerRepository.count(specification);
    }

    /**
     * Function to convert {@link TblCustomerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TblCustomer> createSpecification(TblCustomerCriteria criteria) {
        Specification<TblCustomer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TblCustomer_.id));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), TblCustomer_.status));
            }
            if (criteria.getTotalSpend() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalSpend(), TblCustomer_.totalSpend));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(TblCustomer_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getTaxExemptsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTaxExemptsId(),
                            root -> root.join(TblCustomer_.taxExempts, JoinType.LEFT).get(TblTaxExempt_.id)
                        )
                    );
            }
            if (criteria.getContactsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactsId(),
                            root -> root.join(TblCustomer_.contacts, JoinType.LEFT).get(TblContact_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
