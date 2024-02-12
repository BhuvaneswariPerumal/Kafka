package com.lypd.TrackingService.handler;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.lypd.TrackingService.message.OrderDispatched;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderTracking {
	
	@KafkaListener(
			id="Trackingclient",
			topics="dispatch.tracking",
			groupId = "track.order.created.consumer",
			containerFactory = "kafkaListenerContainerFactory"			
			)
	public void listen(OrderDispatched trackingDetails) {
		log.info("Tracking payload recieved "+trackingDetails);
	}

}
