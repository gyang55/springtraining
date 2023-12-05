package ca.emonster.training.ecommerce.service;

import ca.emonster.training.ecommerce.domain.*; // for static metamodels
import ca.emonster.training.ecommerce.domain.TblInventoryMedia;
import ca.emonster.training.ecommerce.repository.TblInventoryMediaRepository;
import ca.emonster.training.ecommerce.service.criteria.TblInventoryMediaCriteria;
import ca.emonster.training.ecommerce.service.dto.TblInventoryMediaDTO;
import ca.emonster.training.ecommerce.service.mapper.TblInventoryMediaMapper;
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
 * Service for executing complex queries for {@link TblInventoryMedia} entities in the database.
 * The main input is a {@link TblInventoryMediaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TblInventoryMediaDTO} or a {@link Page} of {@link TblInventoryMediaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TblInventoryMediaQueryService extends QueryService<TblInventoryMedia> {

    private final Logger log = LoggerFactory.getLogger(TblInventoryMediaQueryService.class);

    private final TblInventoryMediaRepository tblInventoryMediaRepository;

    private final TblInventoryMediaMapper tblInventoryMediaMapper;

    public TblInventoryMediaQueryService(
        TblInventoryMediaRepository tblInventoryMediaRepository,
        TblInventoryMediaMapper tblInventoryMediaMapper
    ) {
        this.tblInventoryMediaRepository = tblInventoryMediaRepository;
        this.tblInventoryMediaMapper = tblInventoryMediaMapper;
    }

    /**
     * Return a {@link List} of {@link TblInventoryMediaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TblInventoryMediaDTO> findByCriteria(TblInventoryMediaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TblInventoryMedia> specification = createSpecification(criteria);
        return tblInventoryMediaMapper.toDto(tblInventoryMediaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TblInventoryMediaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TblInventoryMediaDTO> findByCriteria(TblInventoryMediaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TblInventoryMedia> specification = createSpecification(criteria);
        return tblInventoryMediaRepository.findAll(specification, page).map(tblInventoryMediaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TblInventoryMediaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TblInventoryMedia> specification = createSpecification(criteria);
        return tblInventoryMediaRepository.count(specification);
    }

    /**
     * Function to convert {@link TblInventoryMediaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TblInventoryMedia> createSpecification(TblInventoryMediaCriteria criteria) {
        Specification<TblInventoryMedia> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TblInventoryMedia_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), TblInventoryMedia_.type));
            }
            if (criteria.getObjectId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObjectId(), TblInventoryMedia_.objectId));
            }
            if (criteria.getKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKey(), TblInventoryMedia_.key));
            }
            if (criteria.getInventoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInventoryId(),
                            root -> root.join(TblInventoryMedia_.inventory, JoinType.LEFT).get(TblInventory_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
