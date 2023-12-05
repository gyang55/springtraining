package ca.emonster.training.ecommerce.repository;

import ca.emonster.training.ecommerce.domain.TblInventory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TblInventory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TblInventoryRepository extends JpaRepository<TblInventory, Long>, JpaSpecificationExecutor<TblInventory> {}
