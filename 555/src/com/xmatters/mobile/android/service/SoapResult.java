package com.xmatters.mobile.android.service;

public class SoapResult {
	private String status = null;
	private String message = null;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean isSuccessful() {
		return "OK".equalsIgnoreCase(status);
	}
	
}
