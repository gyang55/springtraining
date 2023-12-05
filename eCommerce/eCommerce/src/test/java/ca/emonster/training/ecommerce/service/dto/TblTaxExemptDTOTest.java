package ca.emonster.training.ecommerce.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ca.emonster.training.ecommerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblTaxExemptDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblTaxExemptDTO.class);
        TblTaxExemptDTO tblTaxExemptDTO1 = new TblTaxExemptDTO();
        tblTaxExemptDTO1.setId(1L);
        TblTaxExemptDTO tblTaxExemptDTO2 = new TblTaxExemptDTO();
        assertThat(tblTaxExemptDTO1).isNotEqualTo(tblTaxExemptDTO2);
        tblTaxExemptDTO2.setId(tblTaxExemptDTO1.getId());
        assertThat(tblTaxExemptDTO1).isEqualTo(tblTaxExemptDTO2);
        tblTaxExemptDTO2.setId(2L);
        assertThat(tblTaxExemptDTO1).isNotEqualTo(tblTaxExemptDTO2);
        tblTaxExemptDTO1.setId(null);
        assertThat(tblTaxExemptDTO1).isNotEqualTo(tblTaxExemptDTO2);
    }
}
