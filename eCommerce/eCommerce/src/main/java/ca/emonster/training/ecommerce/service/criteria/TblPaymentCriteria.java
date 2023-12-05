package ca.emonster.training.ecommerce.service.criteria;

import ca.emonster.training.ecommerce.domain.enumeration.PaymentChannel;
import ca.emonster.training.ecommerce.domain.enumeration.PaymentMethod;
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
 * Criteria class for the {@link ca.emonster.training.ecommerce.domain.TblPayment} entity. This class is used
 * in {@link ca.emonster.training.ecommerce.web.rest.TblPaymentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tbl-payments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TblPaymentCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PaymentChannel
     */
    public static class PaymentChannelFilter extends Filter<PaymentChannel> {

        public PaymentChannelFilter() {}

        public PaymentChannelFilter(PaymentChannelFilter filter) {
            super(filter);
        }

        @Override
        public PaymentChannelFilter copy() {
            return new PaymentChannelFilter(this);
        }
    }

    /**
     * Class for filtering PaymentMethod
     */
    public static class PaymentMethodFilter extends Filter<PaymentMethod> {

        public PaymentMethodFilter() {}

        public PaymentMethodFilter(PaymentMethodFilter filter) {
            super(filter);
        }

        @Override
        public PaymentMethodFilter copy() {
            return new PaymentMethodFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private PaymentChannelFilter channel;

    private PaymentMethodFilter method;

    private LongFilter orderId;

    private Boolean distinct;

    public TblPaymentCriteria() {}

    public TblPaymentCriteria(TblPaymentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.channel = other.channel == null ? null : other.channel.copy();
        this.method = other.method == null ? null : other.method.copy();
        this.orderId = other.orderId == null ? null : other.orderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TblPaymentCriteria copy() {
        return new TblPaymentCriteria(this);
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

    public PaymentChannelFilter getChannel() {
        return channel;
    }

    public PaymentChannelFilter channel() {
        if (channel == null) {
            channel = new PaymentChannelFilter();
        }
        return channel;
    }

    public void setChannel(PaymentChannelFilter channel) {
        this.channel = channel;
    }

    public PaymentMethodFilter getMethod() {
        return method;
    }

    public PaymentMethodFilter method() {
        if (method == null) {
            method = new PaymentMethodFilter();
        }
        return method;
    }

    public void setMethod(PaymentMethodFilter method) {
        this.method = method;
    }

    public LongFilter getOrderId() {
        return orderId;
    }

    public LongFilter orderId() {
        if (orderId == null) {
            orderId = new LongFilter();
        }
        return orderId;
    }

    public void setOrderId(LongFilter orderId) {
        this.orderId = orderId;
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
        final TblPaymentCriteria that = (TblPaymentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(channel, that.channel) &&
            Objects.equals(method, that.method) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, channel, method, orderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblPaymentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (channel != null ? "channel=" + channel + ", " : "") +
            (method != null ? "method=" + method + ", " : "") +
            (orderId != null ? "orderId=" + orderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
