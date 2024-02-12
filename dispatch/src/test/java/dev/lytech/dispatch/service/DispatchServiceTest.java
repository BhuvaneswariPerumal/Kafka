package dev.lytech.dispatch.service;



import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;  
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import dev.lytech.dispatch.message.OrderCreated;
import dev.lytech.dispatch.message.OrderDispatched;
import dev.lytech.dispatch.util.TestEventData;

public class DispatchServiceTest {
	
	private DispatchService service;
	
	private KafkaTemplate kafkaProducereMock;
	
	@BeforeEach
	void setup() {
		kafkaProducereMock = mock(KafkaTemplate.class);
		service = new DispatchService(kafkaProducereMock);
	}
	@Test
	public void process() throws Exception{
		
		when(kafkaProducereMock.send(anyString(),any(OrderDispatched.class))).thenReturn(mock(CompletableFuture.class));
		OrderCreated testevent = TestEventData.buildOrderCreatedEvent(UUID.randomUUID(),UUID.randomUUID().toString());
		service.process(testevent);
		verify(kafkaProducereMock,times(1)).send(eq("order.dispatched"),any(OrderDispatched.class));
	}
	
	@Test
	public void process_exception() {
		
		//when(kafkaProducereMock.send(anyString(),any(OrderDispatched.class))).thenReturn(mock(CompletableFuture.class));
		OrderCreated testevent = TestEventData.buildOrderCreatedEvent(UUID.randomUUID(),UUID.randomUUID().toString());
		doThrow(new RuntimeException("Producer Failure")).when(kafkaProducereMock).send(eq("order.dispatched"),any(OrderDispatched.class));
		//service.process(testevent);
		Exception exception = assertThrows(RuntimeException.class, () -> service.process(testevent));
		verify(kafkaProducereMock,times(1)).send(eq("order.dispatched"),any(OrderDispatched.class));
		assertThat(exception.getMessage(), equalTo("Producer Failure"));
	}
}
