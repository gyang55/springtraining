package ca.emonster.training.ecommerce.repository;

import ca.emonster.training.ecommerce.domain.TblTaxExempt;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TblTaxExempt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TblTaxExemptRepository extends JpaRepository<TblTaxExempt, Long>, JpaSpecificationExecutor<TblTaxExempt> {}
