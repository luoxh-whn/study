package com.myHelloWorld;

import java.io.Serializable;

public class  Work implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5991934743993742663L;
	private  String address;
	private  String naturework;
	private  String industry;
	private  String application;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getNaturework() {
		return naturework;
	}
	public void setNaturework(String naturework) {
		this.naturework = naturework;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
		
	

}
