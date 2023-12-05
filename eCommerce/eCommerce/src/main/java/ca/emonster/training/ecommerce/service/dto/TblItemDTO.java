package ca.emonster.training.ecommerce.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link ca.emonster.training.ecommerce.domain.TblItem} entity.
 */
public class TblItemDTO implements Serializable {

    private Long id;

    private Long quantity;

    @Lob
    private String remark;

    private TblProductDTO product;

    private TblOrderDTO order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public TblProductDTO getProduct() {
        return product;
    }

    public void setProduct(TblProductDTO product) {
        this.product = product;
    }

    public TblOrderDTO getOrder() {
        return order;
    }

    public void setOrder(TblOrderDTO order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblItemDTO)) {
            return false;
        }

        TblItemDTO tblItemDTO = (TblItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tblItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblItemDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", remark='" + getRemark() + "'" +
            ", product=" + getProduct() +
            ", order=" + getOrder() +
            "}";
    }
}
