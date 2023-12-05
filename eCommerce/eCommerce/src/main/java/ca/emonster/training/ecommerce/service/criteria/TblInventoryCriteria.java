package ca.emonster.training.ecommerce.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link ca.emonster.training.ecommerce.domain.TblInventory} entity. This class is used
 * in {@link ca.emonster.training.ecommerce.web.rest.TblInventoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tbl-inventories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TblInventoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sku;

    private InstantFilter createTime;

    private DoubleFilter weight;

    private LongFilter sotckLevel;

    private LongFilter dimensionId;

    private LongFilter mediaId;

    private LongFilter productsId;

    private Boolean distinct;

    public TblInventoryCriteria() {}

    public TblInventoryCriteria(TblInventoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sku = other.sku == null ? null : other.sku.copy();
        this.createTime = other.createTime == null ? null : other.createTime.copy();
        this.weight = other.weight == null ? null : other.weight.copy();
        this.sotckLevel = other.sotckLevel == null ? null : other.sotckLevel.copy();
        this.dimensionId = other.dimensionId == null ? null : other.dimensionId.copy();
        this.mediaId = other.mediaId == null ? null : other.mediaId.copy();
        this.productsId = other.productsId == null ? null : other.productsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TblInventoryCriteria copy() {
        return new TblInventoryCriteria(this);
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

    public StringFilter getSku() {
        return sku;
    }

    public StringFilter sku() {
        if (sku == null) {
            sku = new StringFilter();
        }
        return sku;
    }

    public void setSku(StringFilter sku) {
        this.sku = sku;
    }

    public InstantFilter getCreateTime() {
        return createTime;
    }

    public InstantFilter createTime() {
        if (createTime == null) {
            createTime = new InstantFilter();
        }
        return createTime;
    }

    public void setCreateTime(InstantFilter createTime) {
        this.createTime = createTime;
    }

    public DoubleFilter getWeight() {
        return weight;
    }

    public DoubleFilter weight() {
        if (weight == null) {
            weight = new DoubleFilter();
        }
        return weight;
    }

    public void setWeight(DoubleFilter weight) {
        this.weight = weight;
    }

    public LongFilter getSotckLevel() {
        return sotckLevel;
    }

    public LongFilter sotckLevel() {
        if (sotckLevel == null) {
            sotckLevel = new LongFilter();
        }
        return sotckLevel;
    }

    public void setSotckLevel(LongFilter sotckLevel) {
        this.sotckLevel = sotckLevel;
    }

    public LongFilter getDimensionId() {
        return dimensionId;
    }

    public LongFilter dimensionId() {
        if (dimensionId == null) {
            dimensionId = new LongFilter();
        }
        return dimensionId;
    }

    public void setDimensionId(LongFilter dimensionId) {
        this.dimensionId = dimensionId;
    }

    public LongFilter getMediaId() {
        return mediaId;
    }

    public LongFilter mediaId() {
        if (mediaId == null) {
            mediaId = new LongFilter();
        }
        return mediaId;
    }

    public void setMediaId(LongFilter mediaId) {
        this.mediaId = mediaId;
    }

    public LongFilter getProductsId() {
        return productsId;
    }

    public LongFilter productsId() {
        if (productsId == null) {
            productsId = new LongFilter();
        }
        return productsId;
    }

    public void setProductsId(LongFilter productsId) {
        this.productsId = productsId;
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
        final TblInventoryCriteria that = (TblInventoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sku, that.sku) &&
            Objects.equals(createTime, that.createTime) &&
            Objects.equals(weight, that.weight) &&
            Objects.equals(sotckLevel, that.sotckLevel) &&
            Objects.equals(dimensionId, that.dimensionId) &&
            Objects.equals(mediaId, that.mediaId) &&
            Objects.equals(productsId, that.productsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sku, createTime, weight, sotckLevel, dimensionId, mediaId, productsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblInventoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (sku != null ? "sku=" + sku + ", " : "") +
            (createTime != null ? "createTime=" + createTime + ", " : "") +
            (weight != null ? "weight=" + weight + ", " : "") +
            (sotckLevel != null ? "sotckLevel=" + sotckLevel + ", " : "") +
            (dimensionId != null ? "dimensionId=" + dimensionId + ", " : "") +
            (mediaId != null ? "mediaId=" + mediaId + ", " : "") +
            (productsId != null ? "productsId=" + productsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
