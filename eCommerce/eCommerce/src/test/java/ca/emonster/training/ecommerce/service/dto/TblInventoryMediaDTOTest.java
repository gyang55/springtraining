package ca.emonster.training.ecommerce.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ca.emonster.training.ecommerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblInventoryMediaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblInventoryMediaDTO.class);
        TblInventoryMediaDTO tblInventoryMediaDTO1 = new TblInventoryMediaDTO();
        tblInventoryMediaDTO1.setId(1L);
        TblInventoryMediaDTO tblInventoryMediaDTO2 = new TblInventoryMediaDTO();
        assertThat(tblInventoryMediaDTO1).isNotEqualTo(tblInventoryMediaDTO2);
        tblInventoryMediaDTO2.setId(tblInventoryMediaDTO1.getId());
        assertThat(tblInventoryMediaDTO1).isEqualTo(tblInventoryMediaDTO2);
        tblInventoryMediaDTO2.setId(2L);
        assertThat(tblInventoryMediaDTO1).isNotEqualTo(tblInventoryMediaDTO2);
        tblInventoryMediaDTO1.setId(null);
        assertThat(tblInventoryMediaDTO1).isNotEqualTo(tblInventoryMediaDTO2);
    }
}
