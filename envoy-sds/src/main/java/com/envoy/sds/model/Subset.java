package com.envoy.sds.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Subset {

	private List<Address> addresses;
	private List<Port> ports;
}
