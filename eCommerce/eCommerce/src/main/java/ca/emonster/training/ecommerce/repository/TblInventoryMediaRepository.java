package ca.emonster.training.ecommerce.repository;

import ca.emonster.training.ecommerce.domain.TblInventoryMedia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TblInventoryMedia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TblInventoryMediaRepository extends JpaRepository<TblInventoryMedia, Long>, JpaSpecificationExecutor<TblInventoryMedia> {}
