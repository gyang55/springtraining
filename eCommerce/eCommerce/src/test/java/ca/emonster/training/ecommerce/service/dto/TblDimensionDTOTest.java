package ca.emonster.training.ecommerce.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ca.emonster.training.ecommerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblDimensionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblDimensionDTO.class);
        TblDimensionDTO tblDimensionDTO1 = new TblDimensionDTO();
        tblDimensionDTO1.setId(1L);
        TblDimensionDTO tblDimensionDTO2 = new TblDimensionDTO();
        assertThat(tblDimensionDTO1).isNotEqualTo(tblDimensionDTO2);
        tblDimensionDTO2.setId(tblDimensionDTO1.getId());
        assertThat(tblDimensionDTO1).isEqualTo(tblDimensionDTO2);
        tblDimensionDTO2.setId(2L);
        assertThat(tblDimensionDTO1).isNotEqualTo(tblDimensionDTO2);
        tblDimensionDTO1.setId(null);
        assertThat(tblDimensionDTO1).isNotEqualTo(tblDimensionDTO2);
    }
}
