package com.rj.se.emp;

import java.net.URL;
import java.util.EventObject;
import java.util.UUID;

import org.apache.camel.management.event.CamelContextStartedEvent;
import org.apache.camel.management.event.CamelContextStoppingEvent;
import org.apache.camel.support.EventNotifierSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.model.agent.ImmutableRegistration;

@Component
public class ConsulNotifier extends EventNotifierSupport {

	@Value("${consul.port}")
	private int port;

	@Value("${consul.host}")
	private String host;
	
	@Value("${exposed.service.name}")
	private String serviceName;

	private String consulServiceId;
	

	@Override
	public boolean isEnabled(EventObject event) {
		return (event instanceof CamelContextStartedEvent || event instanceof CamelContextStoppingEvent);
	}

	/** Ensures that the services are registered during camel initialization and de-registered during camel shutdown */
	public void notify(EventObject event) throws Exception {
		AgentClient client = Consul.builder().withUrl(new URL("http", host, port, "")).build().agentClient();
		if (event instanceof CamelContextStartedEvent) {
			consulServiceId = UUID.randomUUID().toString();
			ImmutableRegistration registration = ImmutableRegistration.builder().id(consulServiceId)
					.name(serviceName).address(host).port(port).build();
			client.register(registration);
		} else if (event instanceof CamelContextStoppingEvent) {
			client.deregister(consulServiceId);
		}
	}

}
