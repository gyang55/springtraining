package ca.emonster.training.ecommerce.service.criteria;

import ca.emonster.training.ecommerce.domain.enumeration.CustomerStatus;
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
 * Criteria class for the {@link ca.emonster.training.ecommerce.domain.TblCustomer} entity. This class is used
 * in {@link ca.emonster.training.ecommerce.web.rest.TblCustomerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tbl-customers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TblCustomerCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CustomerStatus
     */
    public static class CustomerStatusFilter extends Filter<CustomerStatus> {

        public CustomerStatusFilter() {}

        public CustomerStatusFilter(CustomerStatusFilter filter) {
            super(filter);
        }

        @Override
        public CustomerStatusFilter copy() {
            return new CustomerStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private CustomerStatusFilter status;

    private LongFilter totalSpend;

    private LongFilter userId;

    private LongFilter taxExemptsId;

    private LongFilter contactsId;

    private Boolean distinct;

    public TblCustomerCriteria() {}

    public TblCustomerCriteria(TblCustomerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.totalSpend = other.totalSpend == null ? null : other.totalSpend.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.taxExemptsId = other.taxExemptsId == null ? null : other.taxExemptsId.copy();
        this.contactsId = other.contactsId == null ? null : other.contactsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TblCustomerCriteria copy() {
        return new TblCustomerCriteria(this);
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

    public CustomerStatusFilter getStatus() {
        return status;
    }

    public CustomerStatusFilter status() {
        if (status == null) {
            status = new CustomerStatusFilter();
        }
        return status;
    }

    public void setStatus(CustomerStatusFilter status) {
        this.status = status;
    }

    public LongFilter getTotalSpend() {
        return totalSpend;
    }

    public LongFilter totalSpend() {
        if (totalSpend == null) {
            totalSpend = new LongFilter();
        }
        return totalSpend;
    }

    public void setTotalSpend(LongFilter totalSpend) {
        this.totalSpend = totalSpend;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getTaxExemptsId() {
        return taxExemptsId;
    }

    public LongFilter taxExemptsId() {
        if (taxExemptsId == null) {
            taxExemptsId = new LongFilter();
        }
        return taxExemptsId;
    }

    public void setTaxExemptsId(LongFilter taxExemptsId) {
        this.taxExemptsId = taxExemptsId;
    }

    public LongFilter getContactsId() {
        return contactsId;
    }

    public LongFilter contactsId() {
        if (contactsId == null) {
            contactsId = new LongFilter();
        }
        return contactsId;
    }

    public void setContactsId(LongFilter contactsId) {
        this.contactsId = contactsId;
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
        final TblCustomerCriteria that = (TblCustomerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(status, that.status) &&
            Objects.equals(totalSpend, that.totalSpend) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(taxExemptsId, that.taxExemptsId) &&
            Objects.equals(contactsId, that.contactsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, totalSpend, userId, taxExemptsId, contactsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblCustomerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (totalSpend != null ? "totalSpend=" + totalSpend + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (taxExemptsId != null ? "taxExemptsId=" + taxExemptsId + ", " : "") +
            (contactsId != null ? "contactsId=" + contactsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
