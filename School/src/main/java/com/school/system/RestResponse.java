package com.school.system;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RestResponse {
	
	String message;
	boolean stauts;
	int code;
	Object Data;
}
