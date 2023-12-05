package ca.emonster.training.ecommerce.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ca.emonster.training.ecommerce.domain.TblInventory} entity.
 */
public class TblInventoryDTO implements Serializable {

    private Long id;

    private String sku;

    private Instant createTime;

    private Double weight;

    private Long sotckLevel;

    @Lob
    private String description;

    @Lob
    private String remark;

    private TblDimensionDTO dimension;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Long getSotckLevel() {
        return sotckLevel;
    }

    public void setSotckLevel(Long sotckLevel) {
        this.sotckLevel = sotckLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public TblDimensionDTO getDimension() {
        return dimension;
    }

    public void setDimension(TblDimensionDTO dimension) {
        this.dimension = dimension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblInventoryDTO)) {
            return false;
        }

        TblInventoryDTO tblInventoryDTO = (TblInventoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tblInventoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblInventoryDTO{" +
            "id=" + getId() +
            ", sku='" + getSku() + "'" +
            ", createTime='" + getCreateTime() + "'" +
            ", weight=" + getWeight() +
            ", sotckLevel=" + getSotckLevel() +
            ", description='" + getDescription() + "'" +
            ", remark='" + getRemark() + "'" +
            ", dimension=" + getDimension() +
            "}";
    }
}
