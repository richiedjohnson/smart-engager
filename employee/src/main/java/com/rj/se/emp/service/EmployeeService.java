package com.rj.se.emp.service;

import org.springframework.stereotype.Service;

import com.rj.se.emp.model.Employee;

@Service
public class EmployeeService {
	
	public Employee findById(int id){
		return new Employee("Richie",1234);
	}

}
