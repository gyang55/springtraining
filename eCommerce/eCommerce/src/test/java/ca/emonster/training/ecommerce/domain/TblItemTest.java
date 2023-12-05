package ca.emonster.training.ecommerce.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ca.emonster.training.ecommerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblItem.class);
        TblItem tblItem1 = new TblItem();
        tblItem1.setId(1L);
        TblItem tblItem2 = new TblItem();
        tblItem2.setId(tblItem1.getId());
        assertThat(tblItem1).isEqualTo(tblItem2);
        tblItem2.setId(2L);
        assertThat(tblItem1).isNotEqualTo(tblItem2);
        tblItem1.setId(null);
        assertThat(tblItem1).isNotEqualTo(tblItem2);
    }
}
