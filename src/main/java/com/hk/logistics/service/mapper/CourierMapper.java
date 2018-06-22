package com.hk.logistics.service.mapper;

import com.hk.logistics.domain.*;
import com.hk.logistics.service.dto.CourierDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Courier and its DTO CourierDTO.
 */
@Mapper(componentModel = "spring", uses = {CourierChannelMapper.class, CourierGroupMapper.class})
public interface CourierMapper extends EntityMapper<CourierDTO, Courier> {

    @Mapping(source = "courierChannel.id", target = "courierChannelId")
    @Mapping(source = "courierGroup.id", target = "courierGroupId")
    @Mapping(source = "courierGroup.name", target = "courierGroupName")
    CourierDTO toDto(Courier courier);

    @Mapping(source = "courierChannelId", target = "courierChannel")
    @Mapping(source = "courierGroupId", target = "courierGroup")
    Courier toEntity(CourierDTO courierDTO);

    default Courier fromId(Long id) {
        if (id == null) {
            return null;
        }
        Courier courier = new Courier();
        courier.setId(id);
        return courier;
    }
}
