package ca.emonster.training.ecommerce.service.criteria;

import ca.emonster.training.ecommerce.domain.enumeration.TaxType;
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
 * Criteria class for the {@link ca.emonster.training.ecommerce.domain.TblTaxRegion} entity. This class is used
 * in {@link ca.emonster.training.ecommerce.web.rest.TblTaxRegionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tbl-tax-regions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TblTaxRegionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TaxType
     */
    public static class TaxTypeFilter extends Filter<TaxType> {

        public TaxTypeFilter() {}

        public TaxTypeFilter(TaxTypeFilter filter) {
            super(filter);
        }

        @Override
        public TaxTypeFilter copy() {
            return new TaxTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter country;

    private StringFilter state;

    private TaxTypeFilter taxType;

    private DoubleFilter value;

    private LongFilter taxExemptId;

    private Boolean distinct;

    public TblTaxRegionCriteria() {}

    public TblTaxRegionCriteria(TblTaxRegionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.taxType = other.taxType == null ? null : other.taxType.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.taxExemptId = other.taxExemptId == null ? null : other.taxExemptId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TblTaxRegionCriteria copy() {
        return new TblTaxRegionCriteria(this);
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

    public StringFilter getCountry() {
        return country;
    }

    public StringFilter country() {
        if (country == null) {
            country = new StringFilter();
        }
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getState() {
        return state;
    }

    public StringFilter state() {
        if (state == null) {
            state = new StringFilter();
        }
        return state;
    }

    public void setState(StringFilter state) {
        this.state = state;
    }

    public TaxTypeFilter getTaxType() {
        return taxType;
    }

    public TaxTypeFilter taxType() {
        if (taxType == null) {
            taxType = new TaxTypeFilter();
        }
        return taxType;
    }

    public void setTaxType(TaxTypeFilter taxType) {
        this.taxType = taxType;
    }

    public DoubleFilter getValue() {
        return value;
    }

    public DoubleFilter value() {
        if (value == null) {
            value = new DoubleFilter();
        }
        return value;
    }

    public void setValue(DoubleFilter value) {
        this.value = value;
    }

    public LongFilter getTaxExemptId() {
        return taxExemptId;
    }

    public LongFilter taxExemptId() {
        if (taxExemptId == null) {
            taxExemptId = new LongFilter();
        }
        return taxExemptId;
    }

    public void setTaxExemptId(LongFilter taxExemptId) {
        this.taxExemptId = taxExemptId;
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
        final TblTaxRegionCriteria that = (TblTaxRegionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(country, that.country) &&
            Objects.equals(state, that.state) &&
            Objects.equals(taxType, that.taxType) &&
            Objects.equals(value, that.value) &&
            Objects.equals(taxExemptId, that.taxExemptId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, country, state, taxType, value, taxExemptId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblTaxRegionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (country != null ? "country=" + country + ", " : "") +
            (state != null ? "state=" + state + ", " : "") +
            (taxType != null ? "taxType=" + taxType + ", " : "") +
            (value != null ? "value=" + value + ", " : "") +
            (taxExemptId != null ? "taxExemptId=" + taxExemptId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
