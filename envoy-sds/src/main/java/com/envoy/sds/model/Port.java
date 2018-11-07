package com.envoy.sds.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Port {

	private String name;
	private Integer port;
	private String protocol;
}
