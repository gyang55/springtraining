package ca.emonster.training.ecommerce.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TblInventory.
 */
@Entity
@Table(name = "tbl_inventory")
public class TblInventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sku")
    private String sku;

    @Column(name = "create_time")
    private Instant createTime;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "sotck_level")
    private Long sotckLevel;

    @Lob
    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "remark")
    private String remark;

    @JsonIgnoreProperties(value = { "inventory" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private TblDimension dimension;

    @OneToMany(mappedBy = "inventory")
    @JsonIgnoreProperties(value = { "inventory" }, allowSetters = true)
    private Set<TblInventoryMedia> media = new HashSet<>();

    @OneToMany(mappedBy = "inventory")
    @JsonIgnoreProperties(value = { "inventory" }, allowSetters = true)
    private Set<TblProduct> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TblInventory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return this.sku;
    }

    public TblInventory sku(String sku) {
        this.setSku(sku);
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Instant getCreateTime() {
        return this.createTime;
    }

    public TblInventory createTime(Instant createTime) {
        this.setCreateTime(createTime);
        return this;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Double getWeight() {
        return this.weight;
    }

    public TblInventory weight(Double weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Long getSotckLevel() {
        return this.sotckLevel;
    }

    public TblInventory sotckLevel(Long sotckLevel) {
        this.setSotckLevel(sotckLevel);
        return this;
    }

    public void setSotckLevel(Long sotckLevel) {
        this.sotckLevel = sotckLevel;
    }

    public String getDescription() {
        return this.description;
    }

    public TblInventory description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return this.remark;
    }

    public TblInventory remark(String remark) {
        this.setRemark(remark);
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public TblDimension getDimension() {
        return this.dimension;
    }

    public void setDimension(TblDimension tblDimension) {
        this.dimension = tblDimension;
    }

    public TblInventory dimension(TblDimension tblDimension) {
        this.setDimension(tblDimension);
        return this;
    }

    public Set<TblInventoryMedia> getMedia() {
        return this.media;
    }

    public void setMedia(Set<TblInventoryMedia> tblInventoryMedias) {
        if (this.media != null) {
            this.media.forEach(i -> i.setInventory(null));
        }
        if (tblInventoryMedias != null) {
            tblInventoryMedias.forEach(i -> i.setInventory(this));
        }
        this.media = tblInventoryMedias;
    }

    public TblInventory media(Set<TblInventoryMedia> tblInventoryMedias) {
        this.setMedia(tblInventoryMedias);
        return this;
    }

    public TblInventory addMedia(TblInventoryMedia tblInventoryMedia) {
        this.media.add(tblInventoryMedia);
        tblInventoryMedia.setInventory(this);
        return this;
    }

    public TblInventory removeMedia(TblInventoryMedia tblInventoryMedia) {
        this.media.remove(tblInventoryMedia);
        tblInventoryMedia.setInventory(null);
        return this;
    }

    public Set<TblProduct> getProducts() {
        return this.products;
    }

    public void setProducts(Set<TblProduct> tblProducts) {
        if (this.products != null) {
            this.products.forEach(i -> i.setInventory(null));
        }
        if (tblProducts != null) {
            tblProducts.forEach(i -> i.setInventory(this));
        }
        this.products = tblProducts;
    }

    public TblInventory products(Set<TblProduct> tblProducts) {
        this.setProducts(tblProducts);
        return this;
    }

    public TblInventory addProducts(TblProduct tblProduct) {
        this.products.add(tblProduct);
        tblProduct.setInventory(this);
        return this;
    }

    public TblInventory removeProducts(TblProduct tblProduct) {
        this.products.remove(tblProduct);
        tblProduct.setInventory(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblInventory)) {
            return false;
        }
        return id != null && id.equals(((TblInventory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblInventory{" +
            "id=" + getId() +
            ", sku='" + getSku() + "'" +
            ", createTime='" + getCreateTime() + "'" +
            ", weight=" + getWeight() +
            ", sotckLevel=" + getSotckLevel() +
            ", description='" + getDescription() + "'" +
            ", remark='" + getRemark() + "'" +
            "}";
    }
}
