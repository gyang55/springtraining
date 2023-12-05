package ca.emonster.training.ecommerce.repository;

import ca.emonster.training.ecommerce.domain.TblAddress;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TblAddress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TblAddressRepository extends JpaRepository<TblAddress, Long>, JpaSpecificationExecutor<TblAddress> {}
