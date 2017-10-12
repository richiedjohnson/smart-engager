package com.rj.smartengager;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rj.smartengager.model.Employee;

@Component
public class EmployeeRoute extends RouteBuilder {

	@Value("${employee.service.host}")
	private String host;
	
	@Value("${employee.service.port}")
	private String port;

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
		
		JacksonDataFormat formatEmp = new JacksonDataFormat(Employee.class);
		
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
