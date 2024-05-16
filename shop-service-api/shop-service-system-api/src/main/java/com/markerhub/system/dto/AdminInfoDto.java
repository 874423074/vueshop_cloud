package com.markerhub.system.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AdminInfoDto implements Serializable {

	private Long id;
	private String username;
	private String avatar;
	private LocalDateTime created;
}
