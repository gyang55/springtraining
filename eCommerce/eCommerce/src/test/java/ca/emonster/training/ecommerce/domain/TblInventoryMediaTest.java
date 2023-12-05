package ca.emonster.training.ecommerce.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ca.emonster.training.ecommerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblInventoryMediaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblInventoryMedia.class);
        TblInventoryMedia tblInventoryMedia1 = new TblInventoryMedia();
        tblInventoryMedia1.setId(1L);
        TblInventoryMedia tblInventoryMedia2 = new TblInventoryMedia();
        tblInventoryMedia2.setId(tblInventoryMedia1.getId());
        assertThat(tblInventoryMedia1).isEqualTo(tblInventoryMedia2);
        tblInventoryMedia2.setId(2L);
        assertThat(tblInventoryMedia1).isNotEqualTo(tblInventoryMedia2);
        tblInventoryMedia1.setId(null);
        assertThat(tblInventoryMedia1).isNotEqualTo(tblInventoryMedia2);
    }
}
