package ca.emonster.training.ecommerce.domain;

import ca.emonster.training.ecommerce.domain.enumeration.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A TblOrder.
 */
@Entity
@Table(name = "tbl_order")
public class TblOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "order_date")
    private String orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @JsonIgnoreProperties(value = { "order" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private TblPayment payment;

    @JsonIgnoreProperties(value = { "address", "order", "customer" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private TblContact shipTo;

    @OneToMany(mappedBy = "order")
    @JsonIgnoreProperties(value = { "product", "order" }, allowSetters = true)
    private Set<TblItem> items = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "taxExempts", "contacts" }, allowSetters = true)
    private TblCustomer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TblOrder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return this.orderNumber;
    }

    public TblOrder orderNumber(String orderNumber) {
        this.setOrderNumber(orderNumber);
        return this;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderDate() {
        return this.orderDate;
    }

    public TblOrder orderDate(String orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public TblOrder status(OrderStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public TblPayment getPayment() {
        return this.payment;
    }

    public void setPayment(TblPayment tblPayment) {
        this.payment = tblPayment;
    }

    public TblOrder payment(TblPayment tblPayment) {
        this.setPayment(tblPayment);
        return this;
    }

    public TblContact getShipTo() {
        return this.shipTo;
    }

    public void setShipTo(TblContact tblContact) {
        this.shipTo = tblContact;
    }

    public TblOrder shipTo(TblContact tblContact) {
        this.setShipTo(tblContact);
        return this;
    }

    public Set<TblItem> getItems() {
        return this.items;
    }

    public void setItems(Set<TblItem> tblItems) {
        if (this.items != null) {
            this.items.forEach(i -> i.setOrder(null));
        }
        if (tblItems != null) {
            tblItems.forEach(i -> i.setOrder(this));
        }
        this.items = tblItems;
    }

    public TblOrder items(Set<TblItem> tblItems) {
        this.setItems(tblItems);
        return this;
    }

    public TblOrder addItems(TblItem tblItem) {
        this.items.add(tblItem);
        tblItem.setOrder(this);
        return this;
    }

    public TblOrder removeItems(TblItem tblItem) {
        this.items.remove(tblItem);
        tblItem.setOrder(null);
        return this;
    }

    public TblCustomer getCustomer() {
        return this.customer;
    }

    public void setCustomer(TblCustomer tblCustomer) {
        this.customer = tblCustomer;
    }

    public TblOrder customer(TblCustomer tblCustomer) {
        this.setCustomer(tblCustomer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblOrder)) {
            return false;
        }
        return id != null && id.equals(((TblOrder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblOrder{" +
            "id=" + getId() +
            ", orderNumber='" + getOrderNumber() + "'" +
            ", orderDate='" + getOrderDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
