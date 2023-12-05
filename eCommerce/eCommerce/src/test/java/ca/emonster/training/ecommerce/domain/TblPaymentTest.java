package ca.emonster.training.ecommerce.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ca.emonster.training.ecommerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblPaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblPayment.class);
        TblPayment tblPayment1 = new TblPayment();
        tblPayment1.setId(1L);
        TblPayment tblPayment2 = new TblPayment();
        tblPayment2.setId(tblPayment1.getId());
        assertThat(tblPayment1).isEqualTo(tblPayment2);
        tblPayment2.setId(2L);
        assertThat(tblPayment1).isNotEqualTo(tblPayment2);
        tblPayment1.setId(null);
        assertThat(tblPayment1).isNotEqualTo(tblPayment2);
    }
}
