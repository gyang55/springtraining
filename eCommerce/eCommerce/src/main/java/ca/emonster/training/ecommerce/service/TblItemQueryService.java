package ca.emonster.training.ecommerce.service;

import ca.emonster.training.ecommerce.domain.*; // for static metamodels
import ca.emonster.training.ecommerce.domain.TblItem;
import ca.emonster.training.ecommerce.repository.TblItemRepository;
import ca.emonster.training.ecommerce.service.criteria.TblItemCriteria;
import ca.emonster.training.ecommerce.service.dto.TblItemDTO;
import ca.emonster.training.ecommerce.service.mapper.TblItemMapper;
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
 * Service for executing complex queries for {@link TblItem} entities in the database.
 * The main input is a {@link TblItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TblItemDTO} or a {@link Page} of {@link TblItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TblItemQueryService extends QueryService<TblItem> {

    private final Logger log = LoggerFactory.getLogger(TblItemQueryService.class);

    private final TblItemRepository tblItemRepository;

    private final TblItemMapper tblItemMapper;

    public TblItemQueryService(TblItemRepository tblItemRepository, TblItemMapper tblItemMapper) {
        this.tblItemRepository = tblItemRepository;
        this.tblItemMapper = tblItemMapper;
    }

    /**
     * Return a {@link List} of {@link TblItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TblItemDTO> findByCriteria(TblItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TblItem> specification = createSpecification(criteria);
        return tblItemMapper.toDto(tblItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TblItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TblItemDTO> findByCriteria(TblItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TblItem> specification = createSpecification(criteria);
        return tblItemRepository.findAll(specification, page).map(tblItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TblItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TblItem> specification = createSpecification(criteria);
        return tblItemRepository.count(specification);
    }

    /**
     * Function to convert {@link TblItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TblItem> createSpecification(TblItemCriteria criteria) {
        Specification<TblItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TblItem_.id));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), TblItem_.quantity));
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProductId(), root -> root.join(TblItem_.product, JoinType.LEFT).get(TblProduct_.id))
                    );
            }
            if (criteria.getOrderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOrderId(), root -> root.join(TblItem_.order, JoinType.LEFT).get(TblOrder_.id))
                    );
            }
        }
        return specification;
    }
}
