package com.hk.logistics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.hk.logistics.domain.Channel;
import com.hk.logistics.domain.Courier;
import com.hk.logistics.domain.CourierChannel;


/**
 * Spring Data  repository for the CourierChannel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourierChannelRepository extends JpaRepository<CourierChannel, Long>, JpaSpecificationExecutor<CourierChannel> {

	List<CourierChannel> findByChannel(Channel channel);
	CourierChannel findByCourierAndChannel(Courier courier,Channel channel);
}
