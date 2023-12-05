package ca.emonster.training.ecommerce.service.dto;

import ca.emonster.training.ecommerce.domain.enumeration.Currency;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link ca.emonster.training.ecommerce.domain.TblProduct} entity.
 */
public class TblProductDTO implements Serializable {

    private Long id;

    private String displayName;

    private Currency currency;

    private Long price;

    private Boolean isActive;

    @Lob
    private String remark;

    private TblInventoryDTO inventory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        if (!(o instanceof TblProductDTO)) {
            return false;
        }

        TblProductDTO tblProductDTO = (TblProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tblProductDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblProductDTO{" +
            "id=" + getId() +
            ", displayName='" + getDisplayName() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", price=" + getPrice() +
            ", isActive='" + getIsActive() + "'" +
            ", remark='" + getRemark() + "'" +
            ", inventory=" + getInventory() +
            "}";
    }
}
