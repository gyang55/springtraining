package ca.emonster.training.ecommerce.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TblInventoryMediaMapperTest {

    private TblInventoryMediaMapper tblInventoryMediaMapper;

    @BeforeEach
    public void setUp() {
        tblInventoryMediaMapper = new TblInventoryMediaMapperImpl();
    }
}
