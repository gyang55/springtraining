package ca.emonster.training.ecommerce.service.criteria;

import ca.emonster.training.ecommerce.domain.enumeration.Currency;
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
 * Criteria class for the {@link ca.emonster.training.ecommerce.domain.TblProduct} entity. This class is used
 * in {@link ca.emonster.training.ecommerce.web.rest.TblProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tbl-products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TblProductCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Currency
     */
    public static class CurrencyFilter extends Filter<Currency> {

        public CurrencyFilter() {}

        public CurrencyFilter(CurrencyFilter filter) {
            super(filter);
        }

        @Override
        public CurrencyFilter copy() {
            return new CurrencyFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter displayName;

    private CurrencyFilter currency;

    private LongFilter price;

    private BooleanFilter isActive;

    private LongFilter inventoryId;

    private Boolean distinct;

    public TblProductCriteria() {}

    public TblProductCriteria(TblProductCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.displayName = other.displayName == null ? null : other.displayName.copy();
        this.currency = other.currency == null ? null : other.currency.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.inventoryId = other.inventoryId == null ? null : other.inventoryId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TblProductCriteria copy() {
        return new TblProductCriteria(this);
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

    public StringFilter getDisplayName() {
        return displayName;
    }

    public StringFilter displayName() {
        if (displayName == null) {
            displayName = new StringFilter();
        }
        return displayName;
    }

    public void setDisplayName(StringFilter displayName) {
        this.displayName = displayName;
    }

    public CurrencyFilter getCurrency() {
        return currency;
    }

    public CurrencyFilter currency() {
        if (currency == null) {
            currency = new CurrencyFilter();
        }
        return currency;
    }

    public void setCurrency(CurrencyFilter currency) {
        this.currency = currency;
    }

    public LongFilter getPrice() {
        return price;
    }

    public LongFilter price() {
        if (price == null) {
            price = new LongFilter();
        }
        return price;
    }

    public void setPrice(LongFilter price) {
        this.price = price;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            isActive = new BooleanFilter();
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
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
        final TblProductCriteria that = (TblProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(displayName, that.displayName) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(price, that.price) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(inventoryId, that.inventoryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, displayName, currency, price, isActive, inventoryId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblProductCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (displayName != null ? "displayName=" + displayName + ", " : "") +
            (currency != null ? "currency=" + currency + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (inventoryId != null ? "inventoryId=" + inventoryId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
