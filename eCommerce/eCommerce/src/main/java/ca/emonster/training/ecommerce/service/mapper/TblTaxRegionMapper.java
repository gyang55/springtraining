package ca.emonster.training.ecommerce.service.mapper;

import ca.emonster.training.ecommerce.domain.TblTaxRegion;
import ca.emonster.training.ecommerce.service.dto.TblTaxRegionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TblTaxRegion} and its DTO {@link TblTaxRegionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TblTaxRegionMapper extends EntityMapper<TblTaxRegionDTO, TblTaxRegion> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TblTaxRegionDTO toDtoId(TblTaxRegion tblTaxRegion);
}
