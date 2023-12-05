package ca.emonster.training.ecommerce.domain;

import ca.emonster.training.ecommerce.domain.enumeration.ContactType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TblContact.
 */
@Entity
@Table(name = "tbl_contact")
public class TblContact implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "business_name")
    private String businessName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ContactType type;

    @Lob
    @Column(name = "remark")
    private String remark;

    @JsonIgnoreProperties(value = { "contact" }, allowSetters = true)
    @OneToOne(mappedBy = "contact")
    private TblAddress address;

    @JsonIgnoreProperties(value = { "payment", "shipTo", "items", "customer" }, allowSetters = true)
    @OneToOne(mappedBy = "shipTo")
    private TblOrder order;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "taxExempts", "contacts" }, allowSetters = true)
    private TblCustomer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TblContact id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public TblContact firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public TblContact lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public TblContact middleName(String middleName) {
        this.setMiddleName(middleName);
        return this;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getBusinessName() {
        return this.businessName;
    }

    public TblContact businessName(String businessName) {
        this.setBusinessName(businessName);
        return this;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getPhone() {
        return this.phone;
    }

    public TblContact phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public TblContact email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public TblContact isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ContactType getType() {
        return this.type;
    }

    public TblContact type(ContactType type) {
        this.setType(type);
        return this;
    }

    public void setType(ContactType type) {
        this.type = type;
    }

    public String getRemark() {
        return this.remark;
    }

    public TblContact remark(String remark) {
        this.setRemark(remark);
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public TblAddress getAddress() {
        return this.address;
    }

    public void setAddress(TblAddress tblAddress) {
        if (this.address != null) {
            this.address.setContact(null);
        }
        if (tblAddress != null) {
            tblAddress.setContact(this);
        }
        this.address = tblAddress;
    }

    public TblContact address(TblAddress tblAddress) {
        this.setAddress(tblAddress);
        return this;
    }

    public TblOrder getOrder() {
        return this.order;
    }

    public void setOrder(TblOrder tblOrder) {
        if (this.order != null) {
            this.order.setShipTo(null);
        }
        if (tblOrder != null) {
            tblOrder.setShipTo(this);
        }
        this.order = tblOrder;
    }

    public TblContact order(TblOrder tblOrder) {
        this.setOrder(tblOrder);
        return this;
    }

    public TblCustomer getCustomer() {
        return this.customer;
    }

    public void setCustomer(TblCustomer tblCustomer) {
        this.customer = tblCustomer;
    }

    public TblContact customer(TblCustomer tblCustomer) {
        this.setCustomer(tblCustomer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblContact)) {
            return false;
        }
        return id != null && id.equals(((TblContact) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblContact{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", middleName='" + getMiddleName() + "'" +
            ", businessName='" + getBusinessName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", type='" + getType() + "'" +
            ", remark='" + getRemark() + "'" +
            "}";
    }
}
