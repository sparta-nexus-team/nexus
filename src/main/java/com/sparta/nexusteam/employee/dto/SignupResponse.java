package com.sparta.nexusteam.employee.dto;

import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.employee.entity.UserRole;
import lombok.Data;

@Data
public class SignupResponse {
	private Long id;
	private String accountId;
	private String userName;
	private String email;
	private String phoneNumber;
	private String address;
	private UserRole role;

	public SignupResponse(Employee employee) {
		this.id = employee.getId();
		this.accountId = employee.getAccountId();
		this.userName = employee.getUserName();
		this.email = employee.getEmail();
		this.phoneNumber = employee.getPhoneNumber();
		this.address = employee.getAddress();
		this.role = employee.getRole();
	}
}
