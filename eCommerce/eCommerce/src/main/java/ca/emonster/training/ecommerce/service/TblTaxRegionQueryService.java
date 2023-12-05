package ca.emonster.training.ecommerce.service;

import ca.emonster.training.ecommerce.domain.*; // for static metamodels
import ca.emonster.training.ecommerce.domain.TblTaxRegion;
import ca.emonster.training.ecommerce.repository.TblTaxRegionRepository;
import ca.emonster.training.ecommerce.service.criteria.TblTaxRegionCriteria;
import ca.emonster.training.ecommerce.service.dto.TblTaxRegionDTO;
import ca.emonster.training.ecommerce.service.mapper.TblTaxRegionMapper;
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
 * Service for executing complex queries for {@link TblTaxRegion} entities in the database.
 * The main input is a {@link TblTaxRegionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TblTaxRegionDTO} or a {@link Page} of {@link TblTaxRegionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TblTaxRegionQueryService extends QueryService<TblTaxRegion> {

    private final Logger log = LoggerFactory.getLogger(TblTaxRegionQueryService.class);

    private final TblTaxRegionRepository tblTaxRegionRepository;

    private final TblTaxRegionMapper tblTaxRegionMapper;

    public TblTaxRegionQueryService(TblTaxRegionRepository tblTaxRegionRepository, TblTaxRegionMapper tblTaxRegionMapper) {
        this.tblTaxRegionRepository = tblTaxRegionRepository;
        this.tblTaxRegionMapper = tblTaxRegionMapper;
    }

    /**
     * Return a {@link List} of {@link TblTaxRegionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TblTaxRegionDTO> findByCriteria(TblTaxRegionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TblTaxRegion> specification = createSpecification(criteria);
        return tblTaxRegionMapper.toDto(tblTaxRegionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TblTaxRegionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TblTaxRegionDTO> findByCriteria(TblTaxRegionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TblTaxRegion> specification = createSpecification(criteria);
        return tblTaxRegionRepository.findAll(specification, page).map(tblTaxRegionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TblTaxRegionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TblTaxRegion> specification = createSpecification(criteria);
        return tblTaxRegionRepository.count(specification);
    }

    /**
     * Function to convert {@link TblTaxRegionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TblTaxRegion> createSpecification(TblTaxRegionCriteria criteria) {
        Specification<TblTaxRegion> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TblTaxRegion_.id));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), TblTaxRegion_.country));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildStringSpecification(criteria.getState(), TblTaxRegion_.state));
            }
            if (criteria.getTaxType() != null) {
                specification = specification.and(buildSpecification(criteria.getTaxType(), TblTaxRegion_.taxType));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValue(), TblTaxRegion_.value));
            }
            if (criteria.getTaxExemptId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTaxExemptId(),
                            root -> root.join(TblTaxRegion_.taxExempt, JoinType.LEFT).get(TblTaxExempt_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
