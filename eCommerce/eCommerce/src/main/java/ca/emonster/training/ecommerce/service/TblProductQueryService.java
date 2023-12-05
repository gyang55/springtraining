package ca.emonster.training.ecommerce.service;

import ca.emonster.training.ecommerce.domain.*; // for static metamodels
import ca.emonster.training.ecommerce.domain.TblProduct;
import ca.emonster.training.ecommerce.repository.TblProductRepository;
import ca.emonster.training.ecommerce.service.criteria.TblProductCriteria;
import ca.emonster.training.ecommerce.service.dto.TblProductDTO;
import ca.emonster.training.ecommerce.service.mapper.TblProductMapper;
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
 * Service for executing complex queries for {@link TblProduct} entities in the database.
 * The main input is a {@link TblProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TblProductDTO} or a {@link Page} of {@link TblProductDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TblProductQueryService extends QueryService<TblProduct> {

    private final Logger log = LoggerFactory.getLogger(TblProductQueryService.class);

    private final TblProductRepository tblProductRepository;

    private final TblProductMapper tblProductMapper;

    public TblProductQueryService(TblProductRepository tblProductRepository, TblProductMapper tblProductMapper) {
        this.tblProductRepository = tblProductRepository;
        this.tblProductMapper = tblProductMapper;
    }

    /**
     * Return a {@link List} of {@link TblProductDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TblProductDTO> findByCriteria(TblProductCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TblProduct> specification = createSpecification(criteria);
        return tblProductMapper.toDto(tblProductRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TblProductDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TblProductDTO> findByCriteria(TblProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TblProduct> specification = createSpecification(criteria);
        return tblProductRepository.findAll(specification, page).map(tblProductMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TblProductCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TblProduct> specification = createSpecification(criteria);
        return tblProductRepository.count(specification);
    }

    /**
     * Function to convert {@link TblProductCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TblProduct> createSpecification(TblProductCriteria criteria) {
        Specification<TblProduct> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TblProduct_.id));
            }
            if (criteria.getDisplayName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDisplayName(), TblProduct_.displayName));
            }
            if (criteria.getCurrency() != null) {
                specification = specification.and(buildSpecification(criteria.getCurrency(), TblProduct_.currency));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), TblProduct_.price));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), TblProduct_.isActive));
            }
            if (criteria.getInventoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInventoryId(),
                            root -> root.join(TblProduct_.inventory, JoinType.LEFT).get(TblInventory_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
