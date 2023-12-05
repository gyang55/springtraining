package ca.emonster.training.ecommerce.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TblTaxExempt.
 */
@Entity
@Table(name = "tbl_tax_exempt")
public class TblTaxExempt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "exempt_number")
    private String exemptNumber;

    @NotNull
    @Column(name = "effective_start_date", nullable = false)
    private Instant effectiveStartDate;

    @Column(name = "effective_end_date")
    private Instant effectiveEndDate;

    @JsonIgnoreProperties(value = { "taxExempt" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private TblTaxRegion region;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "taxExempts", "contacts" }, allowSetters = true)
    private TblCustomer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TblTaxExempt id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExemptNumber() {
        return this.exemptNumber;
    }

    public TblTaxExempt exemptNumber(String exemptNumber) {
        this.setExemptNumber(exemptNumber);
        return this;
    }

    public void setExemptNumber(String exemptNumber) {
        this.exemptNumber = exemptNumber;
    }

    public Instant getEffectiveStartDate() {
        return this.effectiveStartDate;
    }

    public TblTaxExempt effectiveStartDate(Instant effectiveStartDate) {
        this.setEffectiveStartDate(effectiveStartDate);
        return this;
    }

    public void setEffectiveStartDate(Instant effectiveStartDate) {
        this.effectiveStartDate = effectiveStartDate;
    }

    public Instant getEffectiveEndDate() {
        return this.effectiveEndDate;
    }

    public TblTaxExempt effectiveEndDate(Instant effectiveEndDate) {
        this.setEffectiveEndDate(effectiveEndDate);
        return this;
    }

    public void setEffectiveEndDate(Instant effectiveEndDate) {
        this.effectiveEndDate = effectiveEndDate;
    }

    public TblTaxRegion getRegion() {
        return this.region;
    }

    public void setRegion(TblTaxRegion tblTaxRegion) {
        this.region = tblTaxRegion;
    }

    public TblTaxExempt region(TblTaxRegion tblTaxRegion) {
        this.setRegion(tblTaxRegion);
        return this;
    }

    public TblCustomer getCustomer() {
        return this.customer;
    }

    public void setCustomer(TblCustomer tblCustomer) {
        this.customer = tblCustomer;
    }

    public TblTaxExempt customer(TblCustomer tblCustomer) {
        this.setCustomer(tblCustomer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblTaxExempt)) {
            return false;
        }
        return id != null && id.equals(((TblTaxExempt) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblTaxExempt{" +
            "id=" + getId() +
            ", exemptNumber='" + getExemptNumber() + "'" +
            ", effectiveStartDate='" + getEffectiveStartDate() + "'" +
            ", effectiveEndDate='" + getEffectiveEndDate() + "'" +
            "}";
    }
}
