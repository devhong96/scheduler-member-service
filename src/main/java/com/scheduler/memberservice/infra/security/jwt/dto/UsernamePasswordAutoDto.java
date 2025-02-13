package com.scheduler.memberservice.infra.security.jwt.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsernamePasswordAutoDto {
	private String username;
	private String password;
}
