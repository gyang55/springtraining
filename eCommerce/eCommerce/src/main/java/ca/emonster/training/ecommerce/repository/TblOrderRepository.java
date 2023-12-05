package ca.emonster.training.ecommerce.repository;

import ca.emonster.training.ecommerce.domain.TblOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TblOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TblOrderRepository extends JpaRepository<TblOrder, Long>, JpaSpecificationExecutor<TblOrder> {}
