package se.swedbank.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class VerifyDto {
	private String extendedUsage;
	private String status;
	private String serverTime;
	private String formattedServerTime;
	private Links links;
	public Links getLinks() {
		return links;
	}
	public void setLinks(Links links) {
		this.links = links;
	}
	public String getExtendedUsage() {
		return extendedUsage;
	}
	public void setExtendedUsage(String extendedUsage) {
		this.extendedUsage = extendedUsage;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getServerTime() {
		return serverTime;
	}
	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}
	public String getFormattedServerTime() {
		return formattedServerTime;
	}
	public void setFormattedServerTime(String formattedServerTime) {
		this.formattedServerTime = formattedServerTime;
	}
	
	
	

}
