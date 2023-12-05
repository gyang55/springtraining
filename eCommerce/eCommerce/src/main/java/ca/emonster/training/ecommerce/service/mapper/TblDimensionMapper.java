package ca.emonster.training.ecommerce.service.mapper;

import ca.emonster.training.ecommerce.domain.TblDimension;
import ca.emonster.training.ecommerce.service.dto.TblDimensionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TblDimension} and its DTO {@link TblDimensionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TblDimensionMapper extends EntityMapper<TblDimensionDTO, TblDimension> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TblDimensionDTO toDtoId(TblDimension tblDimension);
}
