package ca.emonster.training.ecommerce.service.mapper;

import ca.emonster.training.ecommerce.domain.TblProduct;
import ca.emonster.training.ecommerce.service.dto.TblProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TblProduct} and its DTO {@link TblProductDTO}.
 */
@Mapper(componentModel = "spring", uses = { TblInventoryMapper.class })
public interface TblProductMapper extends EntityMapper<TblProductDTO, TblProduct> {
    @Mapping(target = "inventory", source = "inventory", qualifiedByName = "id")
    TblProductDTO toDto(TblProduct s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TblProductDTO toDtoId(TblProduct tblProduct);
}
