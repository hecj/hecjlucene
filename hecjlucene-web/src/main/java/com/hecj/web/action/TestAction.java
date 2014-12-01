package com.hecj.web.action;
import com.opensymphony.xwork2.ActionSupport;

public class TestAction extends ActionSupport {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	
	private static final long serialVersionUID = 1L;

	public String test() {
		
		System.out.println("进来了...");
		
		
		return "success" ;
	}

}
