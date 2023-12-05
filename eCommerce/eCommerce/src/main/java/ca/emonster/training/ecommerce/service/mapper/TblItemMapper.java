package ca.emonster.training.ecommerce.service.mapper;

import ca.emonster.training.ecommerce.domain.TblItem;
import ca.emonster.training.ecommerce.service.dto.TblItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TblItem} and its DTO {@link TblItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { TblProductMapper.class, TblOrderMapper.class })
public interface TblItemMapper extends EntityMapper<TblItemDTO, TblItem> {
    @Mapping(target = "product", source = "product", qualifiedByName = "id")
    @Mapping(target = "order", source = "order", qualifiedByName = "id")
    TblItemDTO toDto(TblItem s);
}
