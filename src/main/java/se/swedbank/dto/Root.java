package se.swedbank.dto;

import java.util.List;

public class Root {
	public List<AuthenticationMethod> authenticationMethods;

	public List<AuthenticationMethod> getAuthenticationMethods() {
		return authenticationMethods;
	}

	public void setAuthenticationMethods(List<AuthenticationMethod> authenticationMethods) {
		this.authenticationMethods = authenticationMethods;
	}

}
