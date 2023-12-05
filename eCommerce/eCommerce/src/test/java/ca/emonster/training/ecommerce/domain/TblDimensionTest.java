package ca.emonster.training.ecommerce.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ca.emonster.training.ecommerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblDimensionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblDimension.class);
        TblDimension tblDimension1 = new TblDimension();
        tblDimension1.setId(1L);
        TblDimension tblDimension2 = new TblDimension();
        tblDimension2.setId(tblDimension1.getId());
        assertThat(tblDimension1).isEqualTo(tblDimension2);
        tblDimension2.setId(2L);
        assertThat(tblDimension1).isNotEqualTo(tblDimension2);
        tblDimension1.setId(null);
        assertThat(tblDimension1).isNotEqualTo(tblDimension2);
    }
}
