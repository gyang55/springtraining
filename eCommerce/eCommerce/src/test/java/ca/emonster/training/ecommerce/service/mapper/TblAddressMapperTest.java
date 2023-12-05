package ca.emonster.training.ecommerce.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TblAddressMapperTest {

    private TblAddressMapper tblAddressMapper;

    @BeforeEach
    public void setUp() {
        tblAddressMapper = new TblAddressMapperImpl();
    }
}
