package com.doctor.system;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RestResponse {

	private int code;
	private String message;
	private boolean status;
	private Object data;
	
}
