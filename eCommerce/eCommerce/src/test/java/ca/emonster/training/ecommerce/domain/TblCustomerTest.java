package ca.emonster.training.ecommerce.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ca.emonster.training.ecommerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblCustomerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblCustomer.class);
        TblCustomer tblCustomer1 = new TblCustomer();
        tblCustomer1.setId(1L);
        TblCustomer tblCustomer2 = new TblCustomer();
        tblCustomer2.setId(tblCustomer1.getId());
        assertThat(tblCustomer1).isEqualTo(tblCustomer2);
        tblCustomer2.setId(2L);
        assertThat(tblCustomer1).isNotEqualTo(tblCustomer2);
        tblCustomer1.setId(null);
        assertThat(tblCustomer1).isNotEqualTo(tblCustomer2);
    }
}
