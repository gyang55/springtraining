package ca.emonster.training.ecommerce.service;

import ca.emonster.training.ecommerce.domain.*; // for static metamodels
import ca.emonster.training.ecommerce.domain.TblInventory;
import ca.emonster.training.ecommerce.repository.TblInventoryRepository;
import ca.emonster.training.ecommerce.service.criteria.TblInventoryCriteria;
import ca.emonster.training.ecommerce.service.dto.TblInventoryDTO;
import ca.emonster.training.ecommerce.service.mapper.TblInventoryMapper;
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
 * Service for executing complex queries for {@link TblInventory} entities in the database.
 * The main input is a {@link TblInventoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TblInventoryDTO} or a {@link Page} of {@link TblInventoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TblInventoryQueryService extends QueryService<TblInventory> {

    private final Logger log = LoggerFactory.getLogger(TblInventoryQueryService.class);

    private final TblInventoryRepository tblInventoryRepository;

    private final TblInventoryMapper tblInventoryMapper;

    public TblInventoryQueryService(TblInventoryRepository tblInventoryRepository, TblInventoryMapper tblInventoryMapper) {
        this.tblInventoryRepository = tblInventoryRepository;
        this.tblInventoryMapper = tblInventoryMapper;
    }

    /**
     * Return a {@link List} of {@link TblInventoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TblInventoryDTO> findByCriteria(TblInventoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TblInventory> specification = createSpecification(criteria);
        return tblInventoryMapper.toDto(tblInventoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TblInventoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TblInventoryDTO> findByCriteria(TblInventoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TblInventory> specification = createSpecification(criteria);
        return tblInventoryRepository.findAll(specification, page).map(tblInventoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TblInventoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TblInventory> specification = createSpecification(criteria);
        return tblInventoryRepository.count(specification);
    }

    /**
     * Function to convert {@link TblInventoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TblInventory> createSpecification(TblInventoryCriteria criteria) {
        Specification<TblInventory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TblInventory_.id));
            }
            if (criteria.getSku() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSku(), TblInventory_.sku));
            }
            if (criteria.getCreateTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateTime(), TblInventory_.createTime));
            }
            if (criteria.getWeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeight(), TblInventory_.weight));
            }
            if (criteria.getSotckLevel() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSotckLevel(), TblInventory_.sotckLevel));
            }
            if (criteria.getDimensionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDimensionId(),
                            root -> root.join(TblInventory_.dimension, JoinType.LEFT).get(TblDimension_.id)
                        )
                    );
            }
            if (criteria.getMediaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMediaId(),
                            root -> root.join(TblInventory_.media, JoinType.LEFT).get(TblInventoryMedia_.id)
                        )
                    );
            }
            if (criteria.getProductsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductsId(),
                            root -> root.join(TblInventory_.products, JoinType.LEFT).get(TblProduct_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
