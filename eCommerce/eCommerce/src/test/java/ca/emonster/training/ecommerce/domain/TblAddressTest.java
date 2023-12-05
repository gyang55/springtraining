package ca.emonster.training.ecommerce.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ca.emonster.training.ecommerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblAddressTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblAddress.class);
        TblAddress tblAddress1 = new TblAddress();
        tblAddress1.setId(1L);
        TblAddress tblAddress2 = new TblAddress();
        tblAddress2.setId(tblAddress1.getId());
        assertThat(tblAddress1).isEqualTo(tblAddress2);
        tblAddress2.setId(2L);
        assertThat(tblAddress1).isNotEqualTo(tblAddress2);
        tblAddress1.setId(null);
        assertThat(tblAddress1).isNotEqualTo(tblAddress2);
    }
}
