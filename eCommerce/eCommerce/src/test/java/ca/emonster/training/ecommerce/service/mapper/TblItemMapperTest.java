package ca.emonster.training.ecommerce.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TblItemMapperTest {

    private TblItemMapper tblItemMapper;

    @BeforeEach
    public void setUp() {
        tblItemMapper = new TblItemMapperImpl();
    }
}
