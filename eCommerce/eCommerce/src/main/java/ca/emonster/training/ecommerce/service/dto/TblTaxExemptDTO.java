package ca.emonster.training.ecommerce.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ca.emonster.training.ecommerce.domain.TblTaxExempt} entity.
 */
public class TblTaxExemptDTO implements Serializable {

    private Long id;

    private String exemptNumber;

    @NotNull
    private Instant effectiveStartDate;

    private Instant effectiveEndDate;

    private TblTaxRegionDTO region;

    private TblCustomerDTO customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExemptNumber() {
        return exemptNumber;
    }

    public void setExemptNumber(String exemptNumber) {
        this.exemptNumber = exemptNumber;
    }

    public Instant getEffectiveStartDate() {
        return effectiveStartDate;
    }

    public void setEffectiveStartDate(Instant effectiveStartDate) {
        this.effectiveStartDate = effectiveStartDate;
    }

    public Instant getEffectiveEndDate() {
        return effectiveEndDate;
    }

    public void setEffectiveEndDate(Instant effectiveEndDate) {
        this.effectiveEndDate = effectiveEndDate;
    }

    public TblTaxRegionDTO getRegion() {
        return region;
    }

    public void setRegion(TblTaxRegionDTO region) {
        this.region = region;
    }

    public TblCustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(TblCustomerDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblTaxExemptDTO)) {
            return false;
        }

        TblTaxExemptDTO tblTaxExemptDTO = (TblTaxExemptDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tblTaxExemptDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblTaxExemptDTO{" +
            "id=" + getId() +
            ", exemptNumber='" + getExemptNumber() + "'" +
            ", effectiveStartDate='" + getEffectiveStartDate() + "'" +
            ", effectiveEndDate='" + getEffectiveEndDate() + "'" +
            ", region=" + getRegion() +
            ", customer=" + getCustomer() +
            "}";
    }
}
