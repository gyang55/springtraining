package ca.emonster.training.ecommerce.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ca.emonster.training.ecommerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblContactTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblContact.class);
        TblContact tblContact1 = new TblContact();
        tblContact1.setId(1L);
        TblContact tblContact2 = new TblContact();
        tblContact2.setId(tblContact1.getId());
        assertThat(tblContact1).isEqualTo(tblContact2);
        tblContact2.setId(2L);
        assertThat(tblContact1).isNotEqualTo(tblContact2);
        tblContact1.setId(null);
        assertThat(tblContact1).isNotEqualTo(tblContact2);
    }
}
