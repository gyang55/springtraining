package ca.emonster.training.ecommerce.service.mapper;

import ca.emonster.training.ecommerce.domain.TblTaxExempt;
import ca.emonster.training.ecommerce.service.dto.TblTaxExemptDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TblTaxExempt} and its DTO {@link TblTaxExemptDTO}.
 */
@Mapper(componentModel = "spring", uses = { TblTaxRegionMapper.class, TblCustomerMapper.class })
public interface TblTaxExemptMapper extends EntityMapper<TblTaxExemptDTO, TblTaxExempt> {
    @Mapping(target = "region", source = "region", qualifiedByName = "id")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "id")
    TblTaxExemptDTO toDto(TblTaxExempt s);
}
