package com.sparta.nexusteam.employee.dto;

import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.employee.entity.UserRole;
import lombok.Data;

@Data
public class SignupResponse {
	private Long id;
	private String accountId;
	private String userName;
	private UserRole role;

	public SignupResponse(Employee employee) {
		this.id = employee.getId();
		this.accountId = employee.getAccountId();
		this.userName = employee.getUserName();
		this.role = employee.getRole();
	}
}
