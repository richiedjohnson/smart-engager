package com.rj.se.emp;

import java.util.EventObject;

import org.apache.camel.management.event.CamelContextStartedEvent;
import org.apache.camel.management.event.CamelContextStoppingEvent;
import org.apache.camel.support.EventNotifierSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.model.agent.ImmutableRegistration;

@Component
public class EventNotifier extends EventNotifierSupport {

	@Value("${consul.port}")
	private int port;

	@Value("${consul.host}")
	private String host;

	private final String SERVICE_NAME = "Employee";

	@Override
	public boolean isEnabled(EventObject event) {
		return (event instanceof CamelContextStartedEvent || event instanceof CamelContextStoppingEvent);
	}

	/** Ensures that the services are registered during camel initialization and de-registered during camel shutdown */
	public void notify(EventObject event) throws Exception {
		AgentClient client = Consul.builder().build().agentClient();
		if (event instanceof CamelContextStartedEvent) {
			ImmutableRegistration registration = ImmutableRegistration.builder().id(SERVICE_NAME + port)
					.name(SERVICE_NAME).address(host).port(port).build();
			client.register(registration);
		} else if (event instanceof CamelContextStoppingEvent) {
			client.deregister(SERVICE_NAME + port);
		}
	}

}
