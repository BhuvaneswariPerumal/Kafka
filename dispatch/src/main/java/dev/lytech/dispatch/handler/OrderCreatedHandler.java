package dev.lytech.dispatch.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import dev.lytech.dispatch.message.OrderCreated;
import dev.lytech.dispatch.service.DispatchService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderCreatedHandler {	
	
	public OrderCreatedHandler(DispatchService dispatchService) {
		super();
		this.dispatchService = dispatchService;
	}

	@Autowired
	DispatchService dispatchService;
	
	@KafkaListener(
			id = "orderConsumerClient", 
			topics = "order.created",
			groupId = "dispatch.order.created.consumer",
			containerFactory = "kafkaListenerContainerFactory"
			)
	public void listen(OrderCreated payload) {
		log.info("recieved payload "+ payload);
		try {
		dispatchService.process(payload);
		}catch(Exception e) {
			log.error("Processing Failure "+e);
		}
	}
}
