package ca.emonster.training.ecommerce.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TblInventoryMapperTest {

    private TblInventoryMapper tblInventoryMapper;

    @BeforeEach
    public void setUp() {
        tblInventoryMapper = new TblInventoryMapperImpl();
    }
}
