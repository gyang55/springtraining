package ca.emonster.training.ecommerce.repository;

import ca.emonster.training.ecommerce.domain.TblContact;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TblContact entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TblContactRepository extends JpaRepository<TblContact, Long>, JpaSpecificationExecutor<TblContact> {}
