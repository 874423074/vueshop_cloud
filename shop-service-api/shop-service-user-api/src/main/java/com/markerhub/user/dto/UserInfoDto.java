package com.markerhub.user.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserInfoDto implements Serializable {

	private Long id;
	private String username;
	private String avatar;
	private LocalDateTime created;
}
