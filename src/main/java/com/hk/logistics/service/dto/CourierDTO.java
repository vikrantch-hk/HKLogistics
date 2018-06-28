package com.hk.logistics.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Courier entity.
 */
public class CourierDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Boolean active;

    private String trackingParameter;

    private String trackingUrl;

    private Long parentCourierId;

    private Boolean hkShipping;

    private Boolean vendorShipping;

    private Boolean reversePickup;

    private Long courierGroupId;

    private String courierGroupName;

    private Long courierChannelId;

    private String courierChannelName;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getTrackingParameter() {
        return trackingParameter;
    }

    public void setTrackingParameter(String trackingParameter) {
        this.trackingParameter = trackingParameter;
    }

    public String getTrackingUrl() {
        return trackingUrl;
    }

    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }

    public Long getParentCourierId() {
        return parentCourierId;
    }

    public void setParentCourierId(Long parentCourierId) {
        this.parentCourierId = parentCourierId;
    }

    public Boolean isHkShipping() {
        return hkShipping;
    }

    public void setHkShipping(Boolean hkShipping) {
        this.hkShipping = hkShipping;
    }

    public Boolean isVendorShipping() {
        return vendorShipping;
    }

    public void setVendorShipping(Boolean vendorShipping) {
        this.vendorShipping = vendorShipping;
    }

    public Boolean isReversePickup() {
        return reversePickup;
    }

    public void setReversePickup(Boolean reversePickup) {
        this.reversePickup = reversePickup;
    }

    public Long getCourierGroupId() {
        return courierGroupId;
    }

    public void setCourierGroupId(Long courierGroupId) {
        this.courierGroupId = courierGroupId;
    }

    public String getCourierGroupName() {
        return courierGroupName;
    }

    public void setCourierGroupName(String courierGroupName) {
        this.courierGroupName = courierGroupName;
    }

    public Long getCourierChannelId() {
        return courierChannelId;
    }

    public void setCourierChannelId(Long courierChannelId) {
        this.courierChannelId = courierChannelId;
    }

    public String getCourierChannelName() {
		return courierChannelName;
	}

	public void setCourierChannelName(String courierChannelName) {
		this.courierChannelName = courierChannelName;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CourierDTO courierDTO = (CourierDTO) o;
        if (courierDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), courierDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CourierDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", active='" + isActive() + "'" +
            ", trackingParameter='" + getTrackingParameter() + "'" +
            ", trackingUrl='" + getTrackingUrl() + "'" +
            ", parentCourierId=" + getParentCourierId() +
            ", hkShipping='" + isHkShipping() + "'" +
            ", vendorShipping='" + isVendorShipping() + "'" +
            ", reversePickup='" + isReversePickup() + "'" +
            ", courierGroup=" + getCourierGroupId() +
            ", courierGroup='" + getCourierGroupName() + "'" +
            ", courierChannel=" + getCourierChannelId() +
            "}";
    }
}
