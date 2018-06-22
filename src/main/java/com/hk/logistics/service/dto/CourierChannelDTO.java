package com.hk.logistics.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the CourierChannel entity.
 */
public class CourierChannelDTO implements Serializable {

    private Long id;

    private Double minWeight;

    private Double maxWeight;

    private String natureOfProduct;

    private Long channelId;

    private String channelName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(Double minWeight) {
        this.minWeight = minWeight;
    }

    public Double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(Double maxWeight) {
        this.maxWeight = maxWeight;
    }

    public String getNatureOfProduct() {
        return natureOfProduct;
    }

    public void setNatureOfProduct(String natureOfProduct) {
        this.natureOfProduct = natureOfProduct;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CourierChannelDTO courierChannelDTO = (CourierChannelDTO) o;
        if (courierChannelDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), courierChannelDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CourierChannelDTO{" +
            "id=" + getId() +
            ", minWeight=" + getMinWeight() +
            ", maxWeight=" + getMaxWeight() +
            ", natureOfProduct='" + getNatureOfProduct() + "'" +
            ", channel=" + getChannelId() +
            ", channel='" + getChannelName() + "'" +
            "}";
    }
}
