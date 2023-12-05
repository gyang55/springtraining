package ca.emonster.training.ecommerce.service;

import ca.emonster.training.ecommerce.domain.*; // for static metamodels
import ca.emonster.training.ecommerce.domain.TblDimension;
import ca.emonster.training.ecommerce.repository.TblDimensionRepository;
import ca.emonster.training.ecommerce.service.criteria.TblDimensionCriteria;
import ca.emonster.training.ecommerce.service.dto.TblDimensionDTO;
import ca.emonster.training.ecommerce.service.mapper.TblDimensionMapper;
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
 * Service for executing complex queries for {@link TblDimension} entities in the database.
 * The main input is a {@link TblDimensionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TblDimensionDTO} or a {@link Page} of {@link TblDimensionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TblDimensionQueryService extends QueryService<TblDimension> {

    private final Logger log = LoggerFactory.getLogger(TblDimensionQueryService.class);

    private final TblDimensionRepository tblDimensionRepository;

    private final TblDimensionMapper tblDimensionMapper;

    public TblDimensionQueryService(TblDimensionRepository tblDimensionRepository, TblDimensionMapper tblDimensionMapper) {
        this.tblDimensionRepository = tblDimensionRepository;
        this.tblDimensionMapper = tblDimensionMapper;
    }

    /**
     * Return a {@link List} of {@link TblDimensionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TblDimensionDTO> findByCriteria(TblDimensionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TblDimension> specification = createSpecification(criteria);
        return tblDimensionMapper.toDto(tblDimensionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TblDimensionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TblDimensionDTO> findByCriteria(TblDimensionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TblDimension> specification = createSpecification(criteria);
        return tblDimensionRepository.findAll(specification, page).map(tblDimensionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TblDimensionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TblDimension> specification = createSpecification(criteria);
        return tblDimensionRepository.count(specification);
    }

    /**
     * Function to convert {@link TblDimensionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TblDimension> createSpecification(TblDimensionCriteria criteria) {
        Specification<TblDimension> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TblDimension_.id));
            }
            if (criteria.getLength() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLength(), TblDimension_.length));
            }
            if (criteria.getHeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHeight(), TblDimension_.height));
            }
            if (criteria.getWidth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWidth(), TblDimension_.width));
            }
            if (criteria.getInventoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInventoryId(),
                            root -> root.join(TblDimension_.inventory, JoinType.LEFT).get(TblInventory_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
