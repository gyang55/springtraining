package ca.emonster.training.ecommerce.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ca.emonster.training.ecommerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblTaxRegionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblTaxRegion.class);
        TblTaxRegion tblTaxRegion1 = new TblTaxRegion();
        tblTaxRegion1.setId(1L);
        TblTaxRegion tblTaxRegion2 = new TblTaxRegion();
        tblTaxRegion2.setId(tblTaxRegion1.getId());
        assertThat(tblTaxRegion1).isEqualTo(tblTaxRegion2);
        tblTaxRegion2.setId(2L);
        assertThat(tblTaxRegion1).isNotEqualTo(tblTaxRegion2);
        tblTaxRegion1.setId(null);
        assertThat(tblTaxRegion1).isNotEqualTo(tblTaxRegion2);
    }
}
