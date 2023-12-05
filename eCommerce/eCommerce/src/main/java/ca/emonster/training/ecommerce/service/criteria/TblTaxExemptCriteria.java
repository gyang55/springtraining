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
 * Criteria class for the {@link ca.emonster.training.ecommerce.domain.TblTaxExempt} entity. This class is used
 * in {@link ca.emonster.training.ecommerce.web.rest.TblTaxExemptResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tbl-tax-exempts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TblTaxExemptCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter exemptNumber;

    private InstantFilter effectiveStartDate;

    private InstantFilter effectiveEndDate;

    private LongFilter regionId;

    private LongFilter customerId;

    private Boolean distinct;

    public TblTaxExemptCriteria() {}

    public TblTaxExemptCriteria(TblTaxExemptCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.exemptNumber = other.exemptNumber == null ? null : other.exemptNumber.copy();
        this.effectiveStartDate = other.effectiveStartDate == null ? null : other.effectiveStartDate.copy();
        this.effectiveEndDate = other.effectiveEndDate == null ? null : other.effectiveEndDate.copy();
        this.regionId = other.regionId == null ? null : other.regionId.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TblTaxExemptCriteria copy() {
        return new TblTaxExemptCriteria(this);
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

    public StringFilter getExemptNumber() {
        return exemptNumber;
    }

    public StringFilter exemptNumber() {
        if (exemptNumber == null) {
            exemptNumber = new StringFilter();
        }
        return exemptNumber;
    }

    public void setExemptNumber(StringFilter exemptNumber) {
        this.exemptNumber = exemptNumber;
    }

    public InstantFilter getEffectiveStartDate() {
        return effectiveStartDate;
    }

    public InstantFilter effectiveStartDate() {
        if (effectiveStartDate == null) {
            effectiveStartDate = new InstantFilter();
        }
        return effectiveStartDate;
    }

    public void setEffectiveStartDate(InstantFilter effectiveStartDate) {
        this.effectiveStartDate = effectiveStartDate;
    }

    public InstantFilter getEffectiveEndDate() {
        return effectiveEndDate;
    }

    public InstantFilter effectiveEndDate() {
        if (effectiveEndDate == null) {
            effectiveEndDate = new InstantFilter();
        }
        return effectiveEndDate;
    }

    public void setEffectiveEndDate(InstantFilter effectiveEndDate) {
        this.effectiveEndDate = effectiveEndDate;
    }

    public LongFilter getRegionId() {
        return regionId;
    }

    public LongFilter regionId() {
        if (regionId == null) {
            regionId = new LongFilter();
        }
        return regionId;
    }

    public void setRegionId(LongFilter regionId) {
        this.regionId = regionId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public LongFilter customerId() {
        if (customerId == null) {
            customerId = new LongFilter();
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
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
        final TblTaxExemptCriteria that = (TblTaxExemptCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(exemptNumber, that.exemptNumber) &&
            Objects.equals(effectiveStartDate, that.effectiveStartDate) &&
            Objects.equals(effectiveEndDate, that.effectiveEndDate) &&
            Objects.equals(regionId, that.regionId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, exemptNumber, effectiveStartDate, effectiveEndDate, regionId, customerId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblTaxExemptCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (exemptNumber != null ? "exemptNumber=" + exemptNumber + ", " : "") +
            (effectiveStartDate != null ? "effectiveStartDate=" + effectiveStartDate + ", " : "") +
            (effectiveEndDate != null ? "effectiveEndDate=" + effectiveEndDate + ", " : "") +
            (regionId != null ? "regionId=" + regionId + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
