package ca.emonster.training.ecommerce.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TblContactMapperTest {

    private TblContactMapper tblContactMapper;

    @BeforeEach
    public void setUp() {
        tblContactMapper = new TblContactMapperImpl();
    }
}
