package ca.emonster.training.ecommerce.service.criteria;

import ca.emonster.training.ecommerce.domain.enumeration.OrderStatus;
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
 * Criteria class for the {@link ca.emonster.training.ecommerce.domain.TblOrder} entity. This class is used
 * in {@link ca.emonster.training.ecommerce.web.rest.TblOrderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tbl-orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TblOrderCriteria implements Serializable, Criteria {

    /**
     * Class for filtering OrderStatus
     */
    public static class OrderStatusFilter extends Filter<OrderStatus> {

        public OrderStatusFilter() {}

        public OrderStatusFilter(OrderStatusFilter filter) {
            super(filter);
        }

        @Override
        public OrderStatusFilter copy() {
            return new OrderStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter orderNumber;

    private StringFilter orderDate;

    private OrderStatusFilter status;

    private LongFilter paymentId;

    private LongFilter shipToId;

    private LongFilter itemsId;

    private LongFilter customerId;

    private Boolean distinct;

    public TblOrderCriteria() {}

    public TblOrderCriteria(TblOrderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.orderNumber = other.orderNumber == null ? null : other.orderNumber.copy();
        this.orderDate = other.orderDate == null ? null : other.orderDate.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.paymentId = other.paymentId == null ? null : other.paymentId.copy();
        this.shipToId = other.shipToId == null ? null : other.shipToId.copy();
        this.itemsId = other.itemsId == null ? null : other.itemsId.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TblOrderCriteria copy() {
        return new TblOrderCriteria(this);
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

    public StringFilter getOrderNumber() {
        return orderNumber;
    }

    public StringFilter orderNumber() {
        if (orderNumber == null) {
            orderNumber = new StringFilter();
        }
        return orderNumber;
    }

    public void setOrderNumber(StringFilter orderNumber) {
        this.orderNumber = orderNumber;
    }

    public StringFilter getOrderDate() {
        return orderDate;
    }

    public StringFilter orderDate() {
        if (orderDate == null) {
            orderDate = new StringFilter();
        }
        return orderDate;
    }

    public void setOrderDate(StringFilter orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatusFilter getStatus() {
        return status;
    }

    public OrderStatusFilter status() {
        if (status == null) {
            status = new OrderStatusFilter();
        }
        return status;
    }

    public void setStatus(OrderStatusFilter status) {
        this.status = status;
    }

    public LongFilter getPaymentId() {
        return paymentId;
    }

    public LongFilter paymentId() {
        if (paymentId == null) {
            paymentId = new LongFilter();
        }
        return paymentId;
    }

    public void setPaymentId(LongFilter paymentId) {
        this.paymentId = paymentId;
    }

    public LongFilter getShipToId() {
        return shipToId;
    }

    public LongFilter shipToId() {
        if (shipToId == null) {
            shipToId = new LongFilter();
        }
        return shipToId;
    }

    public void setShipToId(LongFilter shipToId) {
        this.shipToId = shipToId;
    }

    public LongFilter getItemsId() {
        return itemsId;
    }

    public LongFilter itemsId() {
        if (itemsId == null) {
            itemsId = new LongFilter();
        }
        return itemsId;
    }

    public void setItemsId(LongFilter itemsId) {
        this.itemsId = itemsId;
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
        final TblOrderCriteria that = (TblOrderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(orderNumber, that.orderNumber) &&
            Objects.equals(orderDate, that.orderDate) &&
            Objects.equals(status, that.status) &&
            Objects.equals(paymentId, that.paymentId) &&
            Objects.equals(shipToId, that.shipToId) &&
            Objects.equals(itemsId, that.itemsId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderNumber, orderDate, status, paymentId, shipToId, itemsId, customerId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblOrderCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (orderNumber != null ? "orderNumber=" + orderNumber + ", " : "") +
            (orderDate != null ? "orderDate=" + orderDate + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (paymentId != null ? "paymentId=" + paymentId + ", " : "") +
            (shipToId != null ? "shipToId=" + shipToId + ", " : "") +
            (itemsId != null ? "itemsId=" + itemsId + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
