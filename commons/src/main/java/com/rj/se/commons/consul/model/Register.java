package com.rj.se.commons.consul.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Register {
	
	private String ID;
	private String Name;
	private String Address;
	private int Port;

}
