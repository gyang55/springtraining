package ca.emonster.training.ecommerce.service.dto;

import ca.emonster.training.ecommerce.domain.enumeration.OrderStatus;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ca.emonster.training.ecommerce.domain.TblOrder} entity.
 */
public class TblOrderDTO implements Serializable {

    private Long id;

    private String orderNumber;

    private String orderDate;

    private OrderStatus status;

    private TblPaymentDTO payment;

    private TblContactDTO shipTo;

    private TblCustomerDTO customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public TblPaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(TblPaymentDTO payment) {
        this.payment = payment;
    }

    public TblContactDTO getShipTo() {
        return shipTo;
    }

    public void setShipTo(TblContactDTO shipTo) {
        this.shipTo = shipTo;
    }

    public TblCustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(TblCustomerDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblOrderDTO)) {
            return false;
        }

        TblOrderDTO tblOrderDTO = (TblOrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tblOrderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblOrderDTO{" +
            "id=" + getId() +
            ", orderNumber='" + getOrderNumber() + "'" +
            ", orderDate='" + getOrderDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", payment=" + getPayment() +
            ", shipTo=" + getShipTo() +
            ", customer=" + getCustomer() +
            "}";
    }
}
