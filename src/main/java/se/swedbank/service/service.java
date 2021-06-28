package se.swedbank.service;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import se.swedbank.dto.VerifyDto;

@Service
public class service {
	@Autowired
	private RestTemplate restTemplate;
	
	private static final String AuthMethodsApi="https://online.swedbank.se/TDE_DAP_Portal_REST_WEB/api/v5/identification/";
	private static final String AuthUsingBankIdApi="https://online.swedbank.se/TDE_DAP_Portal_REST_WEB/api/v5/identification/bankid/mobile";
	private static final String BarcodeApi = "https://online.swedbank.se/TDE_DAP_Portal_REST_WEB/api/v5/identification/bankid/mobile/image";
	private static final String VerifyApi = "https://online.swedbank.se/TDE_DAP_Portal_REST_WEB/api/v5/identification/bankid/mobile/verify";
	
	/*public static HttpSession resetSessionId(HttpSession session, 
		      HttpServletRequest request) {
		    //session.invalidate();
		    session = request.getSession(true);
		    System.out.println("session Id : " + session);
		    return session;
		}*/	
	
	public String getAuthenticationMethods(){
		
		//Create Headers of the request
		HttpHeaders headers= createHeadersForRequest();
		HttpEntity entity = new HttpEntity(headers);
		
		
		ResponseEntity<String> response = restTemplate.exchange(AuthMethodsApi, HttpMethod.GET, entity, String.class);
		String authenticationMethods = response.getBody();
		System.out.println("Authentication methods : " + authenticationMethods);
		return authenticationMethods;
	}

	
	public void authUsingBankId(String personalNumber) throws IllegalStateException

	{
		//Creating Headers
		HttpHeaders headers= createHeadersForRequest();
		//String static final responseForVerifyApiInString =null;
		//Adding request parameters in Body 
		JSONObject request = new JSONObject();
		try {
			request.put("bankIdOnSameDevice", "false");
			request.put("generateEasyLoginId", "false");
			request.put("useEasyLogin", "false");
			request.put("userId", personalNumber);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		
		HttpEntity entityForAuthWithBankId = new HttpEntity(request.toString(), headers);
		ResponseEntity<String> response = restTemplate.exchange(AuthUsingBankIdApi, HttpMethod.POST, entityForAuthWithBankId, String.class);
		
		//String responseBodyForAuthWithBankId = response.getBody();
		HttpHeaders responseHeaderForAuthWithBankId = response.getHeaders();
		
		/* getJSessionIdFromCookie =getJSessionIdFromCookie(responseHeaderForAuthWithBankId);
		if (getJSessionIdFromCookie.isEmpty())
		{
			System.out.println("Your session is not valid");
			return null;
			
		}*/
	
		
		 Timer timer = new Timer();
	        timer.schedule(new TimerTask() {
	        	String authStatus=null;
	            @Override
	            public void run() {
	                System.out.println("Running: " + new java.util.Date());
	                 authStatus=authenticationStatus(responseHeaderForAuthWithBankId );
	                System.out.println("Running: " + authStatus);
	            }
	        }, 0, 1000);
	   
	
		/*ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.scheduleAtFixedRate(new Runnable() {

		@Override
		public void run() {
			System.out.println("In Every 10 sec");
			
			String authStatus=authenticationStatus(responseHeaderForAuthWithBankId );
			
		}
		}, 0, 10, TimeUnit.SECONDS);*/
		
		//return authStatus;
	}


	private String  authenticationStatus(HttpHeaders responseHeaderForAuthWithBankId)
	{
		HttpHeaders headers= createHeadersForRequest();
		String getJSessionIdFromCookie =getJSessionIdFromCookie(responseHeaderForAuthWithBankId);
		
		headers.set("Cookie", getJSessionIdFromCookie);
		//headers.set("Cookie", responseHeaderForAuthWithBankId.toString());
		HttpEntity entityWithJsessionId = new HttpEntity(headers);
		
		//Image Api invoking
		ResponseEntity<String> responseForBarCodeImage = restTemplate.exchange(BarcodeApi, HttpMethod.GET, entityWithJsessionId, String.class);
		String responseForBarCodeImageInString = responseForBarCodeImage.getBody();
		System.out.println("BarCode to SCAN in String " + responseForBarCodeImageInString);

		//Verify Api invoking
		ResponseEntity<String> responseForVerifyApi = restTemplate.exchange(VerifyApi, HttpMethod.GET, entityWithJsessionId, new ParameterizedTypeReference<String>() {
        });
		//ResponseEntity<List<VerifyDto>> responseForVerifyApi = restTemplate.exchange(VerifyApi, HttpMethod.GET, entityWithJsessionId, new ParameterizedTypeReference<List<VerifyDto>>() {
        //});
		//List<VerifyDto> responseForVerifyApiInString = responseForVerifyApi.getBody();
		String responseForVerifyApiInString = responseForVerifyApi.getBody();
		//System.out.println("Response For Verify API " + responseForVerifyApiInString.get(0).getStatus());
		System.out.println("Response For Verify API " + responseForVerifyApiInString);
		return null;

	}
	private String getJSessionIdFromCookie(HttpHeaders responseHeaderForAuthWithBankId) {
		List<String> valueForCookieKey = responseHeaderForAuthWithBankId.get("Set-cookie");
		String jsessionid = null;
		for (String values : valueForCookieKey) {
			//Get the Cookie values of JSESSION id, as this is the only cookie that is needed for authentication in further API's. 
			//Rest all other cookie values are not needed
			if (values.contains("JSESSIONID")) {
				String cookie1 = values;
				jsessionid = cookie1.split(";")[0];
				System.out.println("Jsession Id " + jsessionid);
				
			}
		}
		return jsessionid;
	}


	private HttpHeaders  createHeadersForRequest() {
		// TODO Auto-generated method stub
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Client", "fdp-internet-bank/180.1.0");
		headers.set("Authorization", "QjdkWkhRY1k3OFZSVno5bDoxNjI0ODA3MzUzMTQy");
		headers.set("Accept", "application/json");
		headers.set("Content-Type", "application/json");
		return headers;
	}
}
