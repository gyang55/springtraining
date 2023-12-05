package ca.emonster.training.ecommerce.service.dto;

import ca.emonster.training.ecommerce.domain.enumeration.PaymentChannel;
import ca.emonster.training.ecommerce.domain.enumeration.PaymentMethod;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ca.emonster.training.ecommerce.domain.TblPayment} entity.
 */
public class TblPaymentDTO implements Serializable {

    private Long id;

    private PaymentChannel channel;

    private PaymentMethod method;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentChannel getChannel() {
        return channel;
    }

    public void setChannel(PaymentChannel channel) {
        this.channel = channel;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblPaymentDTO)) {
            return false;
        }

        TblPaymentDTO tblPaymentDTO = (TblPaymentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tblPaymentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblPaymentDTO{" +
            "id=" + getId() +
            ", channel='" + getChannel() + "'" +
            ", method='" + getMethod() + "'" +
            "}";
    }
}
