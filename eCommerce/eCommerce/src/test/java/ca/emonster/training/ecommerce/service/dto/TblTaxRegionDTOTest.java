package ca.emonster.training.ecommerce.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ca.emonster.training.ecommerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblTaxRegionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblTaxRegionDTO.class);
        TblTaxRegionDTO tblTaxRegionDTO1 = new TblTaxRegionDTO();
        tblTaxRegionDTO1.setId(1L);
        TblTaxRegionDTO tblTaxRegionDTO2 = new TblTaxRegionDTO();
        assertThat(tblTaxRegionDTO1).isNotEqualTo(tblTaxRegionDTO2);
        tblTaxRegionDTO2.setId(tblTaxRegionDTO1.getId());
        assertThat(tblTaxRegionDTO1).isEqualTo(tblTaxRegionDTO2);
        tblTaxRegionDTO2.setId(2L);
        assertThat(tblTaxRegionDTO1).isNotEqualTo(tblTaxRegionDTO2);
        tblTaxRegionDTO1.setId(null);
        assertThat(tblTaxRegionDTO1).isNotEqualTo(tblTaxRegionDTO2);
    }
}
