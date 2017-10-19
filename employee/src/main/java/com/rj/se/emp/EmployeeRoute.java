package com.rj.se.emp;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rj.se.commons.emp.model.Employee;

@Component
public class EmployeeRoute extends RouteBuilder {

	// Values can be overridden as command line args --server.host and --server.port
	@Value("${service.host}")
	private String host;
	
	@Value("${service.port}")
	private String port;
	
	@Value("${consul.host}")
	private String consulHost;
	
	@Value("${consul.port}")
	private int consulPort;

	@Override
	public void configure() throws Exception {
		restConfiguration()
			.component("netty4-http")
			.bindingMode(RestBindingMode.json)
			.host(host)
			.port(port)
			.apiContextPath("/api-doc")
			.apiProperty("api.title", "Employee API")
			.apiProperty("api.version", "1.0")
			.apiProperty("cors", "true");
		
		rest("/employee")
			.get("/{id}")
				.description("Find employee by id")
				.param()
					.name("id")
					.type(RestParamType.path)
					.description("Employee identificator")
					.dataType("int")
					.endParam()
				.outType(Employee.class)
			.to("bean:employeeService?method=findById(${header.id})");

	}

}
