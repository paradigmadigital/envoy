package com.envoy.sds.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Tag {

	private String az;
	private boolean canary;
	private Integer load_balancing_weight;
}
