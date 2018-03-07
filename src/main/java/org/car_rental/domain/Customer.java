package org.car_rental.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Pattern(regexp = "[0-9]+")
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    @Column(name = "street_address", nullable = false)
    private String streetAddress;

    @NotNull
    @Column(name = "age", nullable = false)
    private Integer age;

    @NotNull
    @Column(name = "eligible", nullable = false)
    private Boolean eligible;

    @OneToMany(mappedBy = "done")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Booking> books = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Customer firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Customer lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Customer phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public Customer streetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
        return this;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public Integer getAge() {
        return age;
    }

    public Customer age(Integer age) {
        this.age = age;
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean isEligible() {
        return eligible;
    }

    public Customer eligible(Boolean eligible) {
        this.eligible = eligible;
        return this;
    }

    public void setEligible(Boolean eligible) {
        this.eligible = eligible;
    }

    public Set<Booking> getBooks() {
        return books;
    }

    public Customer books(Set<Booking> bookings) {
        this.books = bookings;
        return this;
    }

    public Customer addBooks(Booking booking) {
        this.books.add(booking);
        booking.setDone(this);
        return this;
    }

    public Customer removeBooks(Booking booking) {
        this.books.remove(booking);
        booking.setDone(null);
        return this;
    }

    public void setBooks(Set<Booking> bookings) {
        this.books = bookings;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer customer = (Customer) o;
        if (customer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", streetAddress='" + getStreetAddress() + "'" +
            ", age=" + getAge() +
            ", eligible='" + isEligible() + "'" +
            "}";
    }
}
