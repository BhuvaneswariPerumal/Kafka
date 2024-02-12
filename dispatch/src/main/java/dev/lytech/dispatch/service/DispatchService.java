package dev.lytech.dispatch.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import dev.lytech.dispatch.message.OrderCreated;
import dev.lytech.dispatch.message.OrderDispatched;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DispatchService {
	
	private static final String ORDER_DISPATCHED_TOPIC = "order.dispatched";
	
	private static final String ORDER_TRACK_TOPIC = "dispatch.tracking";
	
	private final KafkaTemplate<String, Object> kafkaProducer;

	public void process(OrderCreated payload) throws Exception{
		log.info("Process payload " +payload);
		OrderDispatched orderDispatched = OrderDispatched.builder().orderId(payload.getOrderId()).build();
		//kafkaProducer.send(ORDER_DISPATCHED_TOPIC,orderDispatched).get();
		kafkaProducer.send(ORDER_TRACK_TOPIC,orderDispatched).get();
	}
}
