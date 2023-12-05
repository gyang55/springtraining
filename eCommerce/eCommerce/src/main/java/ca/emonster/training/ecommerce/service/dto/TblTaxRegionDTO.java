package ca.emonster.training.ecommerce.service.dto;

import ca.emonster.training.ecommerce.domain.enumeration.TaxType;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ca.emonster.training.ecommerce.domain.TblTaxRegion} entity.
 */
public class TblTaxRegionDTO implements Serializable {

    private Long id;

    @NotNull
    private String country;

    @NotNull
    private String state;

    @NotNull
    private TaxType taxType;

    @NotNull
    private Double value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public TaxType getTaxType() {
        return taxType;
    }

    public void setTaxType(TaxType taxType) {
        this.taxType = taxType;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblTaxRegionDTO)) {
            return false;
        }

        TblTaxRegionDTO tblTaxRegionDTO = (TblTaxRegionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tblTaxRegionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblTaxRegionDTO{" +
            "id=" + getId() +
            ", country='" + getCountry() + "'" +
            ", state='" + getState() + "'" +
            ", taxType='" + getTaxType() + "'" +
            ", value=" + getValue() +
            "}";
    }
}
