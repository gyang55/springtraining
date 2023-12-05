package ca.emonster.training.ecommerce.service;

import ca.emonster.training.ecommerce.domain.*; // for static metamodels
import ca.emonster.training.ecommerce.domain.TblTaxExempt;
import ca.emonster.training.ecommerce.repository.TblTaxExemptRepository;
import ca.emonster.training.ecommerce.service.criteria.TblTaxExemptCriteria;
import ca.emonster.training.ecommerce.service.dto.TblTaxExemptDTO;
import ca.emonster.training.ecommerce.service.mapper.TblTaxExemptMapper;
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
 * Service for executing complex queries for {@link TblTaxExempt} entities in the database.
 * The main input is a {@link TblTaxExemptCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TblTaxExemptDTO} or a {@link Page} of {@link TblTaxExemptDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TblTaxExemptQueryService extends QueryService<TblTaxExempt> {

    private final Logger log = LoggerFactory.getLogger(TblTaxExemptQueryService.class);

    private final TblTaxExemptRepository tblTaxExemptRepository;

    private final TblTaxExemptMapper tblTaxExemptMapper;

    public TblTaxExemptQueryService(TblTaxExemptRepository tblTaxExemptRepository, TblTaxExemptMapper tblTaxExemptMapper) {
        this.tblTaxExemptRepository = tblTaxExemptRepository;
        this.tblTaxExemptMapper = tblTaxExemptMapper;
    }

    /**
     * Return a {@link List} of {@link TblTaxExemptDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TblTaxExemptDTO> findByCriteria(TblTaxExemptCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TblTaxExempt> specification = createSpecification(criteria);
        return tblTaxExemptMapper.toDto(tblTaxExemptRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TblTaxExemptDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TblTaxExemptDTO> findByCriteria(TblTaxExemptCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TblTaxExempt> specification = createSpecification(criteria);
        return tblTaxExemptRepository.findAll(specification, page).map(tblTaxExemptMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TblTaxExemptCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TblTaxExempt> specification = createSpecification(criteria);
        return tblTaxExemptRepository.count(specification);
    }

    /**
     * Function to convert {@link TblTaxExemptCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TblTaxExempt> createSpecification(TblTaxExemptCriteria criteria) {
        Specification<TblTaxExempt> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TblTaxExempt_.id));
            }
            if (criteria.getExemptNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExemptNumber(), TblTaxExempt_.exemptNumber));
            }
            if (criteria.getEffectiveStartDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getEffectiveStartDate(), TblTaxExempt_.effectiveStartDate));
            }
            if (criteria.getEffectiveEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEffectiveEndDate(), TblTaxExempt_.effectiveEndDate));
            }
            if (criteria.getRegionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRegionId(),
                            root -> root.join(TblTaxExempt_.region, JoinType.LEFT).get(TblTaxRegion_.id)
                        )
                    );
            }
            if (criteria.getCustomerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerId(),
                            root -> root.join(TblTaxExempt_.customer, JoinType.LEFT).get(TblCustomer_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
