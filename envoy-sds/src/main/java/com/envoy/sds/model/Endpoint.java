package com.envoy.sds.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Endpoint {

	/* There are more attributes but we don't care about them */
	private List<Subset> subsets; 
}
