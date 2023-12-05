package ca.emonster.training.ecommerce.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A TblItem.
 */
@Entity
@Table(name = "tbl_item")
public class TblItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "quantity")
    private Long quantity;

    @Lob
    @Column(name = "remark")
    private String remark;

    @ManyToOne
    @JsonIgnoreProperties(value = { "inventory" }, allowSetters = true)
    private TblProduct product;

    @ManyToOne
    @JsonIgnoreProperties(value = { "payment", "shipTo", "items", "customer" }, allowSetters = true)
    private TblOrder order;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TblItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public TblItem quantity(Long quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getRemark() {
        return this.remark;
    }

    public TblItem remark(String remark) {
        this.setRemark(remark);
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public TblProduct getProduct() {
        return this.product;
    }

    public void setProduct(TblProduct tblProduct) {
        this.product = tblProduct;
    }

    public TblItem product(TblProduct tblProduct) {
        this.setProduct(tblProduct);
        return this;
    }

    public TblOrder getOrder() {
        return this.order;
    }

    public void setOrder(TblOrder tblOrder) {
        this.order = tblOrder;
    }

    public TblItem order(TblOrder tblOrder) {
        this.setOrder(tblOrder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblItem)) {
            return false;
        }
        return id != null && id.equals(((TblItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblItem{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", remark='" + getRemark() + "'" +
            "}";
    }
}
