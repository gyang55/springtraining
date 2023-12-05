package ca.emonster.training.ecommerce.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ca.emonster.training.ecommerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblProduct.class);
        TblProduct tblProduct1 = new TblProduct();
        tblProduct1.setId(1L);
        TblProduct tblProduct2 = new TblProduct();
        tblProduct2.setId(tblProduct1.getId());
        assertThat(tblProduct1).isEqualTo(tblProduct2);
        tblProduct2.setId(2L);
        assertThat(tblProduct1).isNotEqualTo(tblProduct2);
        tblProduct1.setId(null);
        assertThat(tblProduct1).isNotEqualTo(tblProduct2);
    }
}
