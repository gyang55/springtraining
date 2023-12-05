package ca.emonster.training.ecommerce.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ca.emonster.training.ecommerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblOrder.class);
        TblOrder tblOrder1 = new TblOrder();
        tblOrder1.setId(1L);
        TblOrder tblOrder2 = new TblOrder();
        tblOrder2.setId(tblOrder1.getId());
        assertThat(tblOrder1).isEqualTo(tblOrder2);
        tblOrder2.setId(2L);
        assertThat(tblOrder1).isNotEqualTo(tblOrder2);
        tblOrder1.setId(null);
        assertThat(tblOrder1).isNotEqualTo(tblOrder2);
    }
}
