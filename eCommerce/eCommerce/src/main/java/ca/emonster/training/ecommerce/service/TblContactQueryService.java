package ca.emonster.training.ecommerce.service;

import ca.emonster.training.ecommerce.domain.*; // for static metamodels
import ca.emonster.training.ecommerce.domain.TblContact;
import ca.emonster.training.ecommerce.repository.TblContactRepository;
import ca.emonster.training.ecommerce.service.criteria.TblContactCriteria;
import ca.emonster.training.ecommerce.service.dto.TblContactDTO;
import ca.emonster.training.ecommerce.service.mapper.TblContactMapper;
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
 * Service for executing complex queries for {@link TblContact} entities in the database.
 * The main input is a {@link TblContactCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TblContactDTO} or a {@link Page} of {@link TblContactDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TblContactQueryService extends QueryService<TblContact> {

    private final Logger log = LoggerFactory.getLogger(TblContactQueryService.class);

    private final TblContactRepository tblContactRepository;

    private final TblContactMapper tblContactMapper;

    public TblContactQueryService(TblContactRepository tblContactRepository, TblContactMapper tblContactMapper) {
        this.tblContactRepository = tblContactRepository;
        this.tblContactMapper = tblContactMapper;
    }

    /**
     * Return a {@link List} of {@link TblContactDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TblContactDTO> findByCriteria(TblContactCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TblContact> specification = createSpecification(criteria);
        return tblContactMapper.toDto(tblContactRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TblContactDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TblContactDTO> findByCriteria(TblContactCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TblContact> specification = createSpecification(criteria);
        return tblContactRepository.findAll(specification, page).map(tblContactMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TblContactCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TblContact> specification = createSpecification(criteria);
        return tblContactRepository.count(specification);
    }

    /**
     * Function to convert {@link TblContactCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TblContact> createSpecification(TblContactCriteria criteria) {
        Specification<TblContact> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TblContact_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), TblContact_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), TblContact_.lastName));
            }
            if (criteria.getMiddleName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMiddleName(), TblContact_.middleName));
            }
            if (criteria.getBusinessName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBusinessName(), TblContact_.businessName));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), TblContact_.phone));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), TblContact_.email));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), TblContact_.isActive));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), TblContact_.type));
            }
            if (criteria.getAddressId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAddressId(),
                            root -> root.join(TblContact_.address, JoinType.LEFT).get(TblAddress_.id)
                        )
                    );
            }
            if (criteria.getOrderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOrderId(), root -> root.join(TblContact_.order, JoinType.LEFT).get(TblOrder_.id))
                    );
            }
            if (criteria.getCustomerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerId(),
                            root -> root.join(TblContact_.customer, JoinType.LEFT).get(TblCustomer_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
