package se.swedbank.serviceInterface;

import org.springframework.http.HttpHeaders;



/**
 * @author Ankita
 *
 */
public interface ServiceInterface {
	
	/**
	 * Method Name :getAuthenticationMethods
	 * Description :  Retrieve the possible Authentication methods available for logging in
	 */	
	public void getAuthenticationMethods();
	
	/**
	 * Method Name 	: authUsingBankId
	 * Description 	: Initiate the Authorization by logging in with Bank id
	 * Input		: SocialSecurity Number of user
	 */
	public void authUsingBankId(String personalNumber);
	
	/**
	 * Method Name	: getJSessionIdFromCookie
	 * Description 	: Retrieve the JsessionId for a given Session from cookies
	 * Input 		: Request header Cookie information
	 */
	public String getJSessionIdFromCookie(HttpHeaders responseHeaderForAuthWithBankId) ;
}
