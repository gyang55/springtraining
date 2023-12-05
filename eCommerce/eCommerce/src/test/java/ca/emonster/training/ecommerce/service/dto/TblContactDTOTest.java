package ca.emonster.training.ecommerce.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ca.emonster.training.ecommerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblContactDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblContactDTO.class);
        TblContactDTO tblContactDTO1 = new TblContactDTO();
        tblContactDTO1.setId(1L);
        TblContactDTO tblContactDTO2 = new TblContactDTO();
        assertThat(tblContactDTO1).isNotEqualTo(tblContactDTO2);
        tblContactDTO2.setId(tblContactDTO1.getId());
        assertThat(tblContactDTO1).isEqualTo(tblContactDTO2);
        tblContactDTO2.setId(2L);
        assertThat(tblContactDTO1).isNotEqualTo(tblContactDTO2);
        tblContactDTO1.setId(null);
        assertThat(tblContactDTO1).isNotEqualTo(tblContactDTO2);
    }
}
