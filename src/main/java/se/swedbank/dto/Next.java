package se.swedbank.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


public class Next {
private String method;
private String uri;
public String getMethod() {
	return method;
}
public void setMethod(String method) {
	this.method = method;
}
public String getUri() {
	return uri;
}
public void setUri(String uri) {
	this.uri = uri;
}
}
