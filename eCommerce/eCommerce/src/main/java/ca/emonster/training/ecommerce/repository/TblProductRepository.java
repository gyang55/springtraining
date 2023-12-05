package ca.emonster.training.ecommerce.repository;

import ca.emonster.training.ecommerce.domain.TblProduct;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TblProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TblProductRepository extends JpaRepository<TblProduct, Long>, JpaSpecificationExecutor<TblProduct> {}
