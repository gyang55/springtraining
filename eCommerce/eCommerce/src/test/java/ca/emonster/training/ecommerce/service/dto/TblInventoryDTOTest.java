package ca.emonster.training.ecommerce.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ca.emonster.training.ecommerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblInventoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblInventoryDTO.class);
        TblInventoryDTO tblInventoryDTO1 = new TblInventoryDTO();
        tblInventoryDTO1.setId(1L);
        TblInventoryDTO tblInventoryDTO2 = new TblInventoryDTO();
        assertThat(tblInventoryDTO1).isNotEqualTo(tblInventoryDTO2);
        tblInventoryDTO2.setId(tblInventoryDTO1.getId());
        assertThat(tblInventoryDTO1).isEqualTo(tblInventoryDTO2);
        tblInventoryDTO2.setId(2L);
        assertThat(tblInventoryDTO1).isNotEqualTo(tblInventoryDTO2);
        tblInventoryDTO1.setId(null);
        assertThat(tblInventoryDTO1).isNotEqualTo(tblInventoryDTO2);
    }
}
