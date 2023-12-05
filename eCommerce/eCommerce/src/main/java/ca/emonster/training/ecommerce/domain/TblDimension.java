package ca.emonster.training.ecommerce.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TblDimension.
 */
@Entity
@Table(name = "tbl_dimension")
public class TblDimension implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "length", nullable = false)
    private Double length;

    @NotNull
    @Column(name = "height", nullable = false)
    private Double height;

    @NotNull
    @Column(name = "width", nullable = false)
    private Double width;

    @JsonIgnoreProperties(value = { "dimension", "media", "products" }, allowSetters = true)
    @OneToOne(mappedBy = "dimension")
    private TblInventory inventory;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TblDimension id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLength() {
        return this.length;
    }

    public TblDimension length(Double length) {
        this.setLength(length);
        return this;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getHeight() {
        return this.height;
    }

    public TblDimension height(Double height) {
        this.setHeight(height);
        return this;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWidth() {
        return this.width;
    }

    public TblDimension width(Double width) {
        this.setWidth(width);
        return this;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public TblInventory getInventory() {
        return this.inventory;
    }

    public void setInventory(TblInventory tblInventory) {
        if (this.inventory != null) {
            this.inventory.setDimension(null);
        }
        if (tblInventory != null) {
            tblInventory.setDimension(this);
        }
        this.inventory = tblInventory;
    }

    public TblDimension inventory(TblInventory tblInventory) {
        this.setInventory(tblInventory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblDimension)) {
            return false;
        }
        return id != null && id.equals(((TblDimension) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblDimension{" +
            "id=" + getId() +
            ", length=" + getLength() +
            ", height=" + getHeight() +
            ", width=" + getWidth() +
            "}";
    }
}
