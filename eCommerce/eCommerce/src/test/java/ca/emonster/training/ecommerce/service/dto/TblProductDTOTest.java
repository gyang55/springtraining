package ca.emonster.training.ecommerce.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ca.emonster.training.ecommerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblProductDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblProductDTO.class);
        TblProductDTO tblProductDTO1 = new TblProductDTO();
        tblProductDTO1.setId(1L);
        TblProductDTO tblProductDTO2 = new TblProductDTO();
        assertThat(tblProductDTO1).isNotEqualTo(tblProductDTO2);
        tblProductDTO2.setId(tblProductDTO1.getId());
        assertThat(tblProductDTO1).isEqualTo(tblProductDTO2);
        tblProductDTO2.setId(2L);
        assertThat(tblProductDTO1).isNotEqualTo(tblProductDTO2);
        tblProductDTO1.setId(null);
        assertThat(tblProductDTO1).isNotEqualTo(tblProductDTO2);
    }
}
