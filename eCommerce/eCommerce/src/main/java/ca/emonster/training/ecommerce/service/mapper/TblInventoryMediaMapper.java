package ca.emonster.training.ecommerce.service.mapper;

import ca.emonster.training.ecommerce.domain.TblInventoryMedia;
import ca.emonster.training.ecommerce.service.dto.TblInventoryMediaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TblInventoryMedia} and its DTO {@link TblInventoryMediaDTO}.
 */
@Mapper(componentModel = "spring", uses = { TblInventoryMapper.class })
public interface TblInventoryMediaMapper extends EntityMapper<TblInventoryMediaDTO, TblInventoryMedia> {
    @Mapping(target = "inventory", source = "inventory", qualifiedByName = "id")
    TblInventoryMediaDTO toDto(TblInventoryMedia s);
}
