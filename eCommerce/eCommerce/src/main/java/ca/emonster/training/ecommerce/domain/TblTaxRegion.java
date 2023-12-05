package ca.emonster.training.ecommerce.domain;

import ca.emonster.training.ecommerce.domain.enumeration.TaxType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TblTaxRegion.
 */
@Entity
@Table(name = "tbl_tax_region")
public class TblTaxRegion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "country", nullable = false)
    private String country;

    @NotNull
    @Column(name = "state", nullable = false)
    private String state;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tax_type", nullable = false)
    private TaxType taxType;

    @NotNull
    @Column(name = "value", nullable = false)
    private Double value;

    @JsonIgnoreProperties(value = { "region", "customer" }, allowSetters = true)
    @OneToOne(mappedBy = "region")
    private TblTaxExempt taxExempt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TblTaxRegion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return this.country;
    }

    public TblTaxRegion country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return this.state;
    }

    public TblTaxRegion state(String state) {
        this.setState(state);
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public TaxType getTaxType() {
        return this.taxType;
    }

    public TblTaxRegion taxType(TaxType taxType) {
        this.setTaxType(taxType);
        return this;
    }

    public void setTaxType(TaxType taxType) {
        this.taxType = taxType;
    }

    public Double getValue() {
        return this.value;
    }

    public TblTaxRegion value(Double value) {
        this.setValue(value);
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public TblTaxExempt getTaxExempt() {
        return this.taxExempt;
    }

    public void setTaxExempt(TblTaxExempt tblTaxExempt) {
        if (this.taxExempt != null) {
            this.taxExempt.setRegion(null);
        }
        if (tblTaxExempt != null) {
            tblTaxExempt.setRegion(this);
        }
        this.taxExempt = tblTaxExempt;
    }

    public TblTaxRegion taxExempt(TblTaxExempt tblTaxExempt) {
        this.setTaxExempt(tblTaxExempt);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblTaxRegion)) {
            return false;
        }
        return id != null && id.equals(((TblTaxRegion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblTaxRegion{" +
            "id=" + getId() +
            ", country='" + getCountry() + "'" +
            ", state='" + getState() + "'" +
            ", taxType='" + getTaxType() + "'" +
            ", value=" + getValue() +
            "}";
    }
}
