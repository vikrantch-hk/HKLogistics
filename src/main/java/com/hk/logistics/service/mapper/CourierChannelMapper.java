package com.hk.logistics.service.mapper;

import com.hk.logistics.domain.*;
import com.hk.logistics.service.dto.CourierChannelDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CourierChannel and its DTO CourierChannelDTO.
 */
@Mapper(componentModel = "spring", uses = {ChannelMapper.class})
public interface CourierChannelMapper extends EntityMapper<CourierChannelDTO, CourierChannel> {

    @Mapping(source = "channel.id", target = "channelId")
    @Mapping(source = "channel.name", target = "channelName")
    CourierChannelDTO toDto(CourierChannel courierChannel);

    @Mapping(target = "vendorWHCourierMappings", ignore = true)
    @Mapping(source = "channelId", target = "channel")
    @Mapping(target = "couriers", ignore = true)
    CourierChannel toEntity(CourierChannelDTO courierChannelDTO);

    default CourierChannel fromId(Long id) {
        if (id == null) {
            return null;
        }
        CourierChannel courierChannel = new CourierChannel();
        courierChannel.setId(id);
        return courierChannel;
    }
}
