package dev.lytech.dispatch.handler;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.lytech.dispatch.message.OrderCreated;
import dev.lytech.dispatch.service.DispatchService;
import dev.lytech.dispatch.util.TestEventData;

public class OrderCreatedHandlerTest {
	
	private OrderCreatedHandler handler;
	
	private DispatchService dispatchServiceMock;
	
	@BeforeEach
	void setUp() {
		dispatchServiceMock = mock(DispatchService.class);
		handler = new OrderCreatedHandler(dispatchServiceMock);
	}
	
	@Test
	void listen() throws Exception {
		OrderCreated testevent = TestEventData.buildOrderCreatedEvent(UUID.randomUUID(),UUID.randomUUID().toString());
		handler.listen(testevent);
		verify(dispatchServiceMock,times(1)).process(testevent);
	}
	
	@Test
	void listen_produceException() throws Exception {
		OrderCreated testevent = TestEventData.buildOrderCreatedEvent(UUID.randomUUID(),UUID.randomUUID().toString());
		doThrow(new RuntimeException("service Failure")).when(dispatchServiceMock).process(testevent);
		handler.listen(testevent);
		verify(dispatchServiceMock,times(1)).process(testevent);
	}

}
