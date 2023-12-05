package ca.emonster.training.ecommerce.service.mapper;

import ca.emonster.training.ecommerce.domain.TblOrder;
import ca.emonster.training.ecommerce.service.dto.TblOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TblOrder} and its DTO {@link TblOrderDTO}.
 */
@Mapper(componentModel = "spring", uses = { TblPaymentMapper.class, TblContactMapper.class, TblCustomerMapper.class })
public interface TblOrderMapper extends EntityMapper<TblOrderDTO, TblOrder> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "id")
    @Mapping(target = "shipTo", source = "shipTo", qualifiedByName = "id")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "id")
    TblOrderDTO toDto(TblOrder s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TblOrderDTO toDtoId(TblOrder tblOrder);
}
