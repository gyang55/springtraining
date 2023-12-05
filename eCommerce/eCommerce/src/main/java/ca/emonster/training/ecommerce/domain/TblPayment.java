package ca.emonster.training.ecommerce.domain;

import ca.emonster.training.ecommerce.domain.enumeration.PaymentChannel;
import ca.emonster.training.ecommerce.domain.enumeration.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A TblPayment.
 */
@Entity
@Table(name = "tbl_payment")
public class TblPayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel")
    private PaymentChannel channel;

    @Enumerated(EnumType.STRING)
    @Column(name = "method")
    private PaymentMethod method;

    @JsonIgnoreProperties(value = { "payment", "shipTo", "items", "customer" }, allowSetters = true)
    @OneToOne(mappedBy = "payment")
    private TblOrder order;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TblPayment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentChannel getChannel() {
        return this.channel;
    }

    public TblPayment channel(PaymentChannel channel) {
        this.setChannel(channel);
        return this;
    }

    public void setChannel(PaymentChannel channel) {
        this.channel = channel;
    }

    public PaymentMethod getMethod() {
        return this.method;
    }

    public TblPayment method(PaymentMethod method) {
        this.setMethod(method);
        return this;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public TblOrder getOrder() {
        return this.order;
    }

    public void setOrder(TblOrder tblOrder) {
        if (this.order != null) {
            this.order.setPayment(null);
        }
        if (tblOrder != null) {
            tblOrder.setPayment(this);
        }
        this.order = tblOrder;
    }

    public TblPayment order(TblOrder tblOrder) {
        this.setOrder(tblOrder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblPayment)) {
            return false;
        }
        return id != null && id.equals(((TblPayment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblPayment{" +
            "id=" + getId() +
            ", channel='" + getChannel() + "'" +
            ", method='" + getMethod() + "'" +
            "}";
    }
}
