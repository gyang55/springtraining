package ca.emonster.training.ecommerce.service.mapper;

import ca.emonster.training.ecommerce.domain.TblContact;
import ca.emonster.training.ecommerce.service.dto.TblContactDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TblContact} and its DTO {@link TblContactDTO}.
 */
@Mapper(componentModel = "spring", uses = { TblCustomerMapper.class })
public interface TblContactMapper extends EntityMapper<TblContactDTO, TblContact> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "id")
    TblContactDTO toDto(TblContact s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TblContactDTO toDtoId(TblContact tblContact);
}
