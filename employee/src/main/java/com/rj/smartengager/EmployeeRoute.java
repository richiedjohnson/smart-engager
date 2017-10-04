package com.rj.smartengager;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmployeeRoute extends RouteBuilder {

	@Value("${employee.service.host}")
	private String host;
	
	@Value("${employee.service.port}")
	private String port;

	@Override
	public void configure() throws Exception {
		restConfiguration()
			.component("netty-http")
			.bindingMode(RestBindingMode.json)
			.host(host)
			.port(port);
		
		rest("/employee")
			.get("/{id}")
			.to("bean:employeeService?method=findById(${header.id})");

	}

}
