package ca.emonster.training.ecommerce.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ca.emonster.training.ecommerce.domain.TblDimension} entity.
 */
public class TblDimensionDTO implements Serializable {

    private Long id;

    @NotNull
    private Double length;

    @NotNull
    private Double height;

    @NotNull
    private Double width;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblDimensionDTO)) {
            return false;
        }

        TblDimensionDTO tblDimensionDTO = (TblDimensionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tblDimensionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblDimensionDTO{" +
            "id=" + getId() +
            ", length=" + getLength() +
            ", height=" + getHeight() +
            ", width=" + getWidth() +
            "}";
    }
}
