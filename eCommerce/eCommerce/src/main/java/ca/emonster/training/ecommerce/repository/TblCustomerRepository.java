package ca.emonster.training.ecommerce.repository;

import ca.emonster.training.ecommerce.domain.TblCustomer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TblCustomer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TblCustomerRepository extends JpaRepository<TblCustomer, Long>, JpaSpecificationExecutor<TblCustomer> {}
