package ca.emonster.training.ecommerce.service.mapper;

import ca.emonster.training.ecommerce.domain.TblInventory;
import ca.emonster.training.ecommerce.service.dto.TblInventoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TblInventory} and its DTO {@link TblInventoryDTO}.
 */
@Mapper(componentModel = "spring", uses = { TblDimensionMapper.class })
public interface TblInventoryMapper extends EntityMapper<TblInventoryDTO, TblInventory> {
    @Mapping(target = "dimension", source = "dimension", qualifiedByName = "id")
    TblInventoryDTO toDto(TblInventory s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TblInventoryDTO toDtoId(TblInventory tblInventory);
}
