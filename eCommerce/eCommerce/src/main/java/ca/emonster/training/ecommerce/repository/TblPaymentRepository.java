package ca.emonster.training.ecommerce.repository;

import ca.emonster.training.ecommerce.domain.TblPayment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TblPayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TblPaymentRepository extends JpaRepository<TblPayment, Long>, JpaSpecificationExecutor<TblPayment> {}
