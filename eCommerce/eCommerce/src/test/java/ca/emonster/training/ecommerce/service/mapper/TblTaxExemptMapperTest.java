package ca.emonster.training.ecommerce.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TblTaxExemptMapperTest {

    private TblTaxExemptMapper tblTaxExemptMapper;

    @BeforeEach
    public void setUp() {
        tblTaxExemptMapper = new TblTaxExemptMapperImpl();
    }
}
