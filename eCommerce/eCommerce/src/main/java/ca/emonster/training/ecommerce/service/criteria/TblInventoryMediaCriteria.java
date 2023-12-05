package ca.emonster.training.ecommerce.service.criteria;

import ca.emonster.training.ecommerce.domain.enumeration.InventoryMediaType;
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
 * Criteria class for the {@link ca.emonster.training.ecommerce.domain.TblInventoryMedia} entity. This class is used
 * in {@link ca.emonster.training.ecommerce.web.rest.TblInventoryMediaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tbl-inventory-medias?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TblInventoryMediaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering InventoryMediaType
     */
    public static class InventoryMediaTypeFilter extends Filter<InventoryMediaType> {

        public InventoryMediaTypeFilter() {}

        public InventoryMediaTypeFilter(InventoryMediaTypeFilter filter) {
            super(filter);
        }

        @Override
        public InventoryMediaTypeFilter copy() {
            return new InventoryMediaTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InventoryMediaTypeFilter type;

    private StringFilter objectId;

    private StringFilter key;

    private LongFilter inventoryId;

    private Boolean distinct;

    public TblInventoryMediaCriteria() {}

    public TblInventoryMediaCriteria(TblInventoryMediaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.objectId = other.objectId == null ? null : other.objectId.copy();
        this.key = other.key == null ? null : other.key.copy();
        this.inventoryId = other.inventoryId == null ? null : other.inventoryId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TblInventoryMediaCriteria copy() {
        return new TblInventoryMediaCriteria(this);
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

    public InventoryMediaTypeFilter getType() {
        return type;
    }

    public InventoryMediaTypeFilter type() {
        if (type == null) {
            type = new InventoryMediaTypeFilter();
        }
        return type;
    }

    public void setType(InventoryMediaTypeFilter type) {
        this.type = type;
    }

    public StringFilter getObjectId() {
        return objectId;
    }

    public StringFilter objectId() {
        if (objectId == null) {
            objectId = new StringFilter();
        }
        return objectId;
    }

    public void setObjectId(StringFilter objectId) {
        this.objectId = objectId;
    }

    public StringFilter getKey() {
        return key;
    }

    public StringFilter key() {
        if (key == null) {
            key = new StringFilter();
        }
        return key;
    }

    public void setKey(StringFilter key) {
        this.key = key;
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
        final TblInventoryMediaCriteria that = (TblInventoryMediaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(objectId, that.objectId) &&
            Objects.equals(key, that.key) &&
            Objects.equals(inventoryId, that.inventoryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, objectId, key, inventoryId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblInventoryMediaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (objectId != null ? "objectId=" + objectId + ", " : "") +
            (key != null ? "key=" + key + ", " : "") +
            (inventoryId != null ? "inventoryId=" + inventoryId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
