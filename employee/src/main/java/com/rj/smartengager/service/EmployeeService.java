package com.rj.smartengager.service;

import org.springframework.stereotype.Service;

import com.rj.smartengager.model.Employee;

@Service
public class EmployeeService {
	
	public Employee findById(int id){
		return new Employee("Richie",1234);
	}

}
