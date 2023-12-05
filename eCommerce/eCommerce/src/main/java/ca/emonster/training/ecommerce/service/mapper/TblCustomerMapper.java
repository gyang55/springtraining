package ca.emonster.training.ecommerce.service.mapper;

import ca.emonster.training.ecommerce.domain.TblCustomer;
import ca.emonster.training.ecommerce.service.dto.TblCustomerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TblCustomer} and its DTO {@link TblCustomerDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TblCustomerMapper extends EntityMapper<TblCustomerDTO, TblCustomer> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    TblCustomerDTO toDto(TblCustomer s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TblCustomerDTO toDtoId(TblCustomer tblCustomer);
}
