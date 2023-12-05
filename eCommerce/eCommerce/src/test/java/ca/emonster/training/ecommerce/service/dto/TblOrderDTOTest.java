package ca.emonster.training.ecommerce.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ca.emonster.training.ecommerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblOrderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblOrderDTO.class);
        TblOrderDTO tblOrderDTO1 = new TblOrderDTO();
        tblOrderDTO1.setId(1L);
        TblOrderDTO tblOrderDTO2 = new TblOrderDTO();
        assertThat(tblOrderDTO1).isNotEqualTo(tblOrderDTO2);
        tblOrderDTO2.setId(tblOrderDTO1.getId());
        assertThat(tblOrderDTO1).isEqualTo(tblOrderDTO2);
        tblOrderDTO2.setId(2L);
        assertThat(tblOrderDTO1).isNotEqualTo(tblOrderDTO2);
        tblOrderDTO1.setId(null);
        assertThat(tblOrderDTO1).isNotEqualTo(tblOrderDTO2);
    }
}
