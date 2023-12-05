package ca.emonster.training.ecommerce.service.mapper;

import ca.emonster.training.ecommerce.domain.TblAddress;
import ca.emonster.training.ecommerce.service.dto.TblAddressDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TblAddress} and its DTO {@link TblAddressDTO}.
 */
@Mapper(componentModel = "spring", uses = { TblContactMapper.class })
public interface TblAddressMapper extends EntityMapper<TblAddressDTO, TblAddress> {
    @Mapping(target = "contact", source = "contact", qualifiedByName = "id")
    TblAddressDTO toDto(TblAddress s);
}
