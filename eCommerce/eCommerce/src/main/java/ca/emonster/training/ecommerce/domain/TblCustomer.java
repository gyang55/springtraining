package ca.emonster.training.ecommerce.domain;

import ca.emonster.training.ecommerce.domain.enumeration.CustomerStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A TblCustomer.
 */
@Entity
@Table(name = "tbl_customer")
public class TblCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CustomerStatus status;

    @Column(name = "total_spend")
    private Long totalSpend;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "customer")
    @JsonIgnoreProperties(value = { "region", "customer" }, allowSetters = true)
    private Set<TblTaxExempt> taxExempts = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    @JsonIgnoreProperties(value = { "address", "order", "customer" }, allowSetters = true)
    private Set<TblContact> contacts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TblCustomer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerStatus getStatus() {
        return this.status;
    }

    public TblCustomer status(CustomerStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    public Long getTotalSpend() {
        return this.totalSpend;
    }

    public TblCustomer totalSpend(Long totalSpend) {
        this.setTotalSpend(totalSpend);
        return this;
    }

    public void setTotalSpend(Long totalSpend) {
        this.totalSpend = totalSpend;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TblCustomer user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<TblTaxExempt> getTaxExempts() {
        return this.taxExempts;
    }

    public void setTaxExempts(Set<TblTaxExempt> tblTaxExempts) {
        if (this.taxExempts != null) {
            this.taxExempts.forEach(i -> i.setCustomer(null));
        }
        if (tblTaxExempts != null) {
            tblTaxExempts.forEach(i -> i.setCustomer(this));
        }
        this.taxExempts = tblTaxExempts;
    }

    public TblCustomer taxExempts(Set<TblTaxExempt> tblTaxExempts) {
        this.setTaxExempts(tblTaxExempts);
        return this;
    }

    public TblCustomer addTaxExempts(TblTaxExempt tblTaxExempt) {
        this.taxExempts.add(tblTaxExempt);
        tblTaxExempt.setCustomer(this);
        return this;
    }

    public TblCustomer removeTaxExempts(TblTaxExempt tblTaxExempt) {
        this.taxExempts.remove(tblTaxExempt);
        tblTaxExempt.setCustomer(null);
        return this;
    }

    public Set<TblContact> getContacts() {
        return this.contacts;
    }

    public void setContacts(Set<TblContact> tblContacts) {
        if (this.contacts != null) {
            this.contacts.forEach(i -> i.setCustomer(null));
        }
        if (tblContacts != null) {
            tblContacts.forEach(i -> i.setCustomer(this));
        }
        this.contacts = tblContacts;
    }

    public TblCustomer contacts(Set<TblContact> tblContacts) {
        this.setContacts(tblContacts);
        return this;
    }

    public TblCustomer addContacts(TblContact tblContact) {
        this.contacts.add(tblContact);
        tblContact.setCustomer(this);
        return this;
    }

    public TblCustomer removeContacts(TblContact tblContact) {
        this.contacts.remove(tblContact);
        tblContact.setCustomer(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TblCustomer)) {
            return false;
        }
        return id != null && id.equals(((TblCustomer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TblCustomer{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", totalSpend=" + getTotalSpend() +
            "}";
    }
}
