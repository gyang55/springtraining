package ca.emonster.training.ecommerce.service.dto;

import ca.emonster.training.ecommerce.domain.enumeration.InventoryMediaType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ca.emonster.training.ecommerce.domain.TblInventoryMedia} entity.
 */
public class TblInventoryMediaDTO implements Serializable {

    private Long id;

    private InventoryMediaType type;

    private String objectId;

    private String key;

    private TblInventoryDTO inventory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InventoryMediaType getType() {
        return type;
    }

    public void setType(InventoryMediaType type) {
        this.type = type;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public TblInventoryDTO getInventory() {
        return inventory;
    }

    public void setInventory(TblInventoryDTO inventory) {
        this.inventory = inventory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblInventoryMediaDTO)) {
            return false;
        }

        TblInventoryMediaDTO tblInventoryMediaDTO = (TblInventoryMediaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tblInventoryMediaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblInventoryMediaDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", objectId='" + getObjectId() + "'" +
            ", key='" + getKey() + "'" +
            ", inventory=" + getInventory() +
            "}";
    }
}
