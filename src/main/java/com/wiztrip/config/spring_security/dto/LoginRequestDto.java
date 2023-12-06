package com.wiztrip.config.spring_security.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
	private String username;
	private String password;
}
