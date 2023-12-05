package ca.emonster.training.ecommerce.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ca.emonster.training.ecommerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblPaymentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblPaymentDTO.class);
        TblPaymentDTO tblPaymentDTO1 = new TblPaymentDTO();
        tblPaymentDTO1.setId(1L);
        TblPaymentDTO tblPaymentDTO2 = new TblPaymentDTO();
        assertThat(tblPaymentDTO1).isNotEqualTo(tblPaymentDTO2);
        tblPaymentDTO2.setId(tblPaymentDTO1.getId());
        assertThat(tblPaymentDTO1).isEqualTo(tblPaymentDTO2);
        tblPaymentDTO2.setId(2L);
        assertThat(tblPaymentDTO1).isNotEqualTo(tblPaymentDTO2);
        tblPaymentDTO1.setId(null);
        assertThat(tblPaymentDTO1).isNotEqualTo(tblPaymentDTO2);
    }
}
