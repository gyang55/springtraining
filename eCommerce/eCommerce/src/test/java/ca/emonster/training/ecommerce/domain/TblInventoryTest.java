package ca.emonster.training.ecommerce.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ca.emonster.training.ecommerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblInventoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblInventory.class);
        TblInventory tblInventory1 = new TblInventory();
        tblInventory1.setId(1L);
        TblInventory tblInventory2 = new TblInventory();
        tblInventory2.setId(tblInventory1.getId());
        assertThat(tblInventory1).isEqualTo(tblInventory2);
        tblInventory2.setId(2L);
        assertThat(tblInventory1).isNotEqualTo(tblInventory2);
        tblInventory1.setId(null);
        assertThat(tblInventory1).isNotEqualTo(tblInventory2);
    }
}
