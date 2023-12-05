package ca.emonster.training.ecommerce.service.mapper;

import ca.emonster.training.ecommerce.domain.TblPayment;
import ca.emonster.training.ecommerce.service.dto.TblPaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TblPayment} and its DTO {@link TblPaymentDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TblPaymentMapper extends EntityMapper<TblPaymentDTO, TblPayment> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TblPaymentDTO toDtoId(TblPayment tblPayment);
}
