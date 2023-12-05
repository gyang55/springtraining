package ca.emonster.training.ecommerce.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ca.emonster.training.ecommerce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TblItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TblItemDTO.class);
        TblItemDTO tblItemDTO1 = new TblItemDTO();
        tblItemDTO1.setId(1L);
        TblItemDTO tblItemDTO2 = new TblItemDTO();
        assertThat(tblItemDTO1).isNotEqualTo(tblItemDTO2);
        tblItemDTO2.setId(tblItemDTO1.getId());
        assertThat(tblItemDTO1).isEqualTo(tblItemDTO2);
        tblItemDTO2.setId(2L);
        assertThat(tblItemDTO1).isNotEqualTo(tblItemDTO2);
        tblItemDTO1.setId(null);
        assertThat(tblItemDTO1).isNotEqualTo(tblItemDTO2);
    }
}
