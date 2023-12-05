package ca.emonster.training.ecommerce.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link ca.emonster.training.ecommerce.domain.TblDimension} entity. This class is used
 * in {@link ca.emonster.training.ecommerce.web.rest.TblDimensionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tbl-dimensions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TblDimensionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter length;

    private DoubleFilter height;

    private DoubleFilter width;

    private LongFilter inventoryId;

    private Boolean distinct;

    public TblDimensionCriteria() {}

    public TblDimensionCriteria(TblDimensionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.length = other.length == null ? null : other.length.copy();
        this.height = other.height == null ? null : other.height.copy();
        this.width = other.width == null ? null : other.width.copy();
        this.inventoryId = other.inventoryId == null ? null : other.inventoryId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TblDimensionCriteria copy() {
        return new TblDimensionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getLength() {
        return length;
    }

    public DoubleFilter length() {
        if (length == null) {
            length = new DoubleFilter();
        }
        return length;
    }

    public void setLength(DoubleFilter length) {
        this.length = length;
    }

    public DoubleFilter getHeight() {
        return height;
    }

    public DoubleFilter height() {
        if (height == null) {
            height = new DoubleFilter();
        }
        return height;
    }

    public void setHeight(DoubleFilter height) {
        this.height = height;
    }

    public DoubleFilter getWidth() {
        return width;
    }

    public DoubleFilter width() {
        if (width == null) {
            width = new DoubleFilter();
        }
        return width;
    }

    public void setWidth(DoubleFilter width) {
        this.width = width;
    }

    public LongFilter getInventoryId() {
        return inventoryId;
    }

    public LongFilter inventoryId() {
        if (inventoryId == null) {
            inventoryId = new LongFilter();
        }
        return inventoryId;
    }

    public void setInventoryId(LongFilter inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TblDimensionCriteria that = (TblDimensionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(length, that.length) &&
            Objects.equals(height, that.height) &&
            Objects.equals(width, that.width) &&
            Objects.equals(inventoryId, that.inventoryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, length, height, width, inventoryId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblDimensionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (length != null ? "length=" + length + ", " : "") +
            (height != null ? "height=" + height + ", " : "") +
            (width != null ? "width=" + width + ", " : "") +
            (inventoryId != null ? "inventoryId=" + inventoryId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
