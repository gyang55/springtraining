package ca.emonster.training.ecommerce.repository;

import ca.emonster.training.ecommerce.domain.TblTaxRegion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TblTaxRegion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TblTaxRegionRepository extends JpaRepository<TblTaxRegion, Long>, JpaSpecificationExecutor<TblTaxRegion> {}
