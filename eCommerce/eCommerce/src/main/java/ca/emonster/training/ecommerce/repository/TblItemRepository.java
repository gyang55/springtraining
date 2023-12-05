package ca.emonster.training.ecommerce.repository;

import ca.emonster.training.ecommerce.domain.TblItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TblItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TblItemRepository extends JpaRepository<TblItem, Long>, JpaSpecificationExecutor<TblItem> {}
