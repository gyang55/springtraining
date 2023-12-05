package ca.emonster.training.ecommerce.domain;

import ca.emonster.training.ecommerce.domain.enumeration.InventoryMediaType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A TblInventoryMedia.
 */
@Entity
@Table(name = "tbl_inventory_media")
public class TblInventoryMedia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private InventoryMediaType type;

    @Column(name = "object_id")
    private String objectId;

    @Column(name = "jhi_key")
    private String key;

    @ManyToOne
    @JsonIgnoreProperties(value = { "dimension", "media", "products" }, allowSetters = true)
    private TblInventory inventory;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TblInventoryMedia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InventoryMediaType getType() {
        return this.type;
    }

    public TblInventoryMedia type(InventoryMediaType type) {
        this.setType(type);
        return this;
    }

    public void setType(InventoryMediaType type) {
        this.type = type;
    }

    public String getObjectId() {
        return this.objectId;
    }

    public TblInventoryMedia objectId(String objectId) {
        this.setObjectId(objectId);
        return this;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getKey() {
        return this.key;
    }

    public TblInventoryMedia key(String key) {
        this.setKey(key);
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public TblInventory getInventory() {
        return this.inventory;
    }

    public void setInventory(TblInventory tblInventory) {
        this.inventory = tblInventory;
    }

    public TblInventoryMedia inventory(TblInventory tblInventory) {
        this.setInventory(tblInventory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblInventoryMedia)) {
            return false;
        }
        return id != null && id.equals(((TblInventoryMedia) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblInventoryMedia{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", objectId='" + getObjectId() + "'" +
            ", key='" + getKey() + "'" +
            "}";
    }
}
