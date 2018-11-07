package com.envoy.sds.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Host {

	private String ip_address;
	private Integer port;
	private Tag tags;
}
