package com.jt.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EasyUITree {

	private Long id;
	private String text;
	private String state;
	
}
