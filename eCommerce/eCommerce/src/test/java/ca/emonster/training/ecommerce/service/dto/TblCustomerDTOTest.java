package ca.emonster.training.ecommerce.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ca.emonster.training.ecommerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblCustomerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblCustomerDTO.class);
        TblCustomerDTO tblCustomerDTO1 = new TblCustomerDTO();
        tblCustomerDTO1.setId(1L);
        TblCustomerDTO tblCustomerDTO2 = new TblCustomerDTO();
        assertThat(tblCustomerDTO1).isNotEqualTo(tblCustomerDTO2);
        tblCustomerDTO2.setId(tblCustomerDTO1.getId());
        assertThat(tblCustomerDTO1).isEqualTo(tblCustomerDTO2);
        tblCustomerDTO2.setId(2L);
        assertThat(tblCustomerDTO1).isNotEqualTo(tblCustomerDTO2);
        tblCustomerDTO1.setId(null);
        assertThat(tblCustomerDTO1).isNotEqualTo(tblCustomerDTO2);
    }
}
