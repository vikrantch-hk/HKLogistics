package com.hk.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Courier.
 */
@Entity
@Table(name = "courier")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "courier")
public class Courier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "tracking_parameter")
    private String trackingParameter;

    @Column(name = "tracking_url")
    private String trackingUrl;

    @Column(name = "parent_courier_id")
    private Long parentCourierId;

    @Column(name = "hk_shipping")
    private Boolean hkShipping;

    @Column(name = "vendor_shipping")
    private Boolean vendorShipping;

    @Column(name = "reverse_pickup")
    private Boolean reversePickup;

    @ManyToOne
    @JsonIgnoreProperties("couriers")
    private CourierGroup courierGroup;

    @ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
    @JsonIgnoreProperties("couriers")
    private CourierChannel courierChannel;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Courier name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isActive() {
        return active;
    }

    public Courier active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getTrackingParameter() {
        return trackingParameter;
    }

    public Courier trackingParameter(String trackingParameter) {
        this.trackingParameter = trackingParameter;
        return this;
    }

    public void setTrackingParameter(String trackingParameter) {
        this.trackingParameter = trackingParameter;
    }

    public String getTrackingUrl() {
        return trackingUrl;
    }

    public Courier trackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
        return this;
    }

    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }

    public Long getParentCourierId() {
        return parentCourierId;
    }

    public Courier parentCourierId(Long parentCourierId) {
        this.parentCourierId = parentCourierId;
        return this;
    }

    public void setParentCourierId(Long parentCourierId) {
        this.parentCourierId = parentCourierId;
    }

    public Boolean isHkShipping() {
        return hkShipping;
    }

    public Courier hkShipping(Boolean hkShipping) {
        this.hkShipping = hkShipping;
        return this;
    }

    public void setHkShipping(Boolean hkShipping) {
        this.hkShipping = hkShipping;
    }

    public Boolean isVendorShipping() {
        return vendorShipping;
    }

    public Courier vendorShipping(Boolean vendorShipping) {
        this.vendorShipping = vendorShipping;
        return this;
    }

    public void setVendorShipping(Boolean vendorShipping) {
        this.vendorShipping = vendorShipping;
    }

    public Boolean isReversePickup() {
        return reversePickup;
    }

    public Courier reversePickup(Boolean reversePickup) {
        this.reversePickup = reversePickup;
        return this;
    }

    public void setReversePickup(Boolean reversePickup) {
        this.reversePickup = reversePickup;
    }

    public CourierGroup getCourierGroup() {
        return courierGroup;
    }

    public Courier courierGroup(CourierGroup courierGroup) {
        this.courierGroup = courierGroup;
        return this;
    }

    public void setCourierGroup(CourierGroup courierGroup) {
        this.courierGroup = courierGroup;
    }

    public CourierChannel getCourierChannel() {
        return courierChannel;
    }

    public Courier courierChannel(CourierChannel courierChannel) {
        this.courierChannel = courierChannel;
        return this;
    }

    public void setCourierChannel(CourierChannel courierChannel) {
        this.courierChannel = courierChannel;
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
        Courier courier = (Courier) o;
        if (courier.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), courier.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Courier{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", active='" + isActive() + "'" +
            ", trackingParameter='" + getTrackingParameter() + "'" +
            ", trackingUrl='" + getTrackingUrl() + "'" +
            ", parentCourierId=" + getParentCourierId() +
            ", hkShipping='" + isHkShipping() + "'" +
            ", vendorShipping='" + isVendorShipping() + "'" +
            ", reversePickup='" + isReversePickup() + "'" +
            "}";
    }
}
