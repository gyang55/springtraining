package ca.emonster.training.ecommerce.service.dto;

import ca.emonster.training.ecommerce.domain.enumeration.CustomerStatus;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ca.emonster.training.ecommerce.domain.TblCustomer} entity.
 */
public class TblCustomerDTO implements Serializable {

    private Long id;

    private CustomerStatus status;

    private Long totalSpend;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    public Long getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(Long totalSpend) {
        this.totalSpend = totalSpend;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblCustomerDTO)) {
            return false;
        }

        TblCustomerDTO tblCustomerDTO = (TblCustomerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tblCustomerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblCustomerDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", totalSpend=" + getTotalSpend() +
            ", user=" + getUser() +
            "}";
    }
}
