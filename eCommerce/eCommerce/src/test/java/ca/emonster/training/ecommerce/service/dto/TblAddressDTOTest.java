package ca.emonster.training.ecommerce.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ca.emonster.training.ecommerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblAddressDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblAddressDTO.class);
        TblAddressDTO tblAddressDTO1 = new TblAddressDTO();
        tblAddressDTO1.setId(1L);
        TblAddressDTO tblAddressDTO2 = new TblAddressDTO();
        assertThat(tblAddressDTO1).isNotEqualTo(tblAddressDTO2);
        tblAddressDTO2.setId(tblAddressDTO1.getId());
        assertThat(tblAddressDTO1).isEqualTo(tblAddressDTO2);
        tblAddressDTO2.setId(2L);
        assertThat(tblAddressDTO1).isNotEqualTo(tblAddressDTO2);
        tblAddressDTO1.setId(null);
        assertThat(tblAddressDTO1).isNotEqualTo(tblAddressDTO2);
    }
}
