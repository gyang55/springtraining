package ca.emonster.training.ecommerce.service;

import ca.emonster.training.ecommerce.domain.*; // for static metamodels
import ca.emonster.training.ecommerce.domain.TblAddress;
import ca.emonster.training.ecommerce.repository.TblAddressRepository;
import ca.emonster.training.ecommerce.service.criteria.TblAddressCriteria;
import ca.emonster.training.ecommerce.service.dto.TblAddressDTO;
import ca.emonster.training.ecommerce.service.mapper.TblAddressMapper;
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
 * Service for executing complex queries for {@link TblAddress} entities in the database.
 * The main input is a {@link TblAddressCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TblAddressDTO} or a {@link Page} of {@link TblAddressDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TblAddressQueryService extends QueryService<TblAddress> {

    private final Logger log = LoggerFactory.getLogger(TblAddressQueryService.class);

    private final TblAddressRepository tblAddressRepository;

    private final TblAddressMapper tblAddressMapper;

    public TblAddressQueryService(TblAddressRepository tblAddressRepository, TblAddressMapper tblAddressMapper) {
        this.tblAddressRepository = tblAddressRepository;
        this.tblAddressMapper = tblAddressMapper;
    }

    /**
     * Return a {@link List} of {@link TblAddressDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TblAddressDTO> findByCriteria(TblAddressCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TblAddress> specification = createSpecification(criteria);
        return tblAddressMapper.toDto(tblAddressRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TblAddressDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TblAddressDTO> findByCriteria(TblAddressCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TblAddress> specification = createSpecification(criteria);
        return tblAddressRepository.findAll(specification, page).map(tblAddressMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TblAddressCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TblAddress> specification = createSpecification(criteria);
        return tblAddressRepository.count(specification);
    }

    /**
     * Function to convert {@link TblAddressCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TblAddress> createSpecification(TblAddressCriteria criteria) {
        Specification<TblAddress> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TblAddress_.id));
            }
            if (criteria.getUnit() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUnit(), TblAddress_.unit));
            }
            if (criteria.getAddress1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress1(), TblAddress_.address1));
            }
            if (criteria.getAddress2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress2(), TblAddress_.address2));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), TblAddress_.city));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildStringSpecification(criteria.getState(), TblAddress_.state));
            }
            if (criteria.getPostalCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPostalCode(), TblAddress_.postalCode));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), TblAddress_.country));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), TblAddress_.isActive));
            }
            if (criteria.getContactId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactId(),
                            root -> root.join(TblAddress_.contact, JoinType.LEFT).get(TblContact_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
