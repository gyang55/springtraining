package ca.emonster.training.ecommerce.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ca.emonster.training.ecommerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblTaxExemptTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblTaxExempt.class);
        TblTaxExempt tblTaxExempt1 = new TblTaxExempt();
        tblTaxExempt1.setId(1L);
        TblTaxExempt tblTaxExempt2 = new TblTaxExempt();
        tblTaxExempt2.setId(tblTaxExempt1.getId());
        assertThat(tblTaxExempt1).isEqualTo(tblTaxExempt2);
        tblTaxExempt2.setId(2L);
        assertThat(tblTaxExempt1).isNotEqualTo(tblTaxExempt2);
        tblTaxExempt1.setId(null);
        assertThat(tblTaxExempt1).isNotEqualTo(tblTaxExempt2);
    }
}
