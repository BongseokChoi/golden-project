package com.iandna.project.config.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class ResultModel extends HashMap {
	public ResultModel() {
		// TODO Auto-generated constructor stub
	}
	
	public ResultModel(int actFlag) {
		this.put("actFlag", actFlag);
	}
	
	public ResultModel(int actFlag, String resMsg) {
		this.put("actFlag", actFlag);
		this.put("resMsg", resMsg);
	}
	
	
	
	public static void main(String[] args) {
		ResultModel r = new ResultModel(1, "asdasd");
		
		System.out.println(r.toString());
		System.out.println(r.get("actFlag"));
	}
}
