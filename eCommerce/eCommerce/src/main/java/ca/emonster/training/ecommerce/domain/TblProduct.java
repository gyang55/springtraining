package ca.emonster.training.ecommerce.domain;

import ca.emonster.training.ecommerce.domain.enumeration.Currency;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A TblProduct.
 */
@Entity
@Table(name = "tbl_product")
public class TblProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "display_name")
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

    @Column(name = "price")
    private Long price;

    @Column(name = "is_active")
    private Boolean isActive;

    @Lob
    @Column(name = "remark")
    private String remark;

    @ManyToOne
    @JsonIgnoreProperties(value = { "dimension", "media", "products" }, allowSetters = true)
    private TblInventory inventory;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TblProduct id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public TblProduct displayName(String displayName) {
        this.setDisplayName(displayName);
        return this;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public TblProduct currency(Currency currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Long getPrice() {
        return this.price;
    }

    public TblProduct price(Long price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public TblProduct isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getRemark() {
        return this.remark;
    }

    public TblProduct remark(String remark) {
        this.setRemark(remark);
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public TblInventory getInventory() {
        return this.inventory;
    }

    public void setInventory(TblInventory tblInventory) {
        this.inventory = tblInventory;
    }

    public TblProduct inventory(TblInventory tblInventory) {
        this.setInventory(tblInventory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblProduct)) {
            return false;
        }
        return id != null && id.equals(((TblProduct) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblProduct{" +
            "id=" + getId() +
            ", displayName='" + getDisplayName() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", price=" + getPrice() +
            ", isActive='" + getIsActive() + "'" +
            ", remark='" + getRemark() + "'" +
            "}";
    }
}
