package se.swedbank.service;

import java.io.IOException;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.swedbank.dto.AuthenticationMethod;
import se.swedbank.dto.Root;
import se.swedbank.dto.VerifyDto;
import se.swedbank.serviceInterface.ServiceInterface;

@org.springframework.stereotype.Service
public class Service implements ServiceInterface {
	@Autowired
	private RestTemplate restTemplate;
	
	private static final String AuthMethodsApi="https://online.swedbank.se/TDE_DAP_Portal_REST_WEB/api/v5/identification/";
	private static final String AuthUsingBankIdApi="https://online.swedbank.se/TDE_DAP_Portal_REST_WEB/api/v5/identification/bankid/mobile";
	private static final String BarcodeApi = "https://online.swedbank.se/TDE_DAP_Portal_REST_WEB/api/v5/identification/bankid/mobile/image";
	private static final String VerifyApi = "https://online.swedbank.se/TDE_DAP_Portal_REST_WEB/api/v5/identification/bankid/mobile/verify";
	
	
	public void getAuthenticationMethods(){
		
		//Create Headers of the request
		HttpHeaders headers= createHeadersForRequest();
		HttpEntity entity = new HttpEntity(headers);
		
		Root jsonResponse=null;
		
		ResponseEntity<String> response = restTemplate.exchange(AuthMethodsApi, HttpMethod.GET, entity, String.class);
		String authenticationMethods = response.getBody();
		ObjectMapper objectMapper = new ObjectMapper();

		// Deserialization into the Root class , i.e converting into JSON object 
		try {
			 jsonResponse = objectMapper.readValue(authenticationMethods, Root.class);
			 System.out.println("Authentication methods : " );
			 
			 for (AuthenticationMethod eachAuthMethod:jsonResponse.getAuthenticationMethods() )
			 {
				 System.out.println(eachAuthMethod.getMessage()+ "\n");
			 }
			
		} catch (JsonParseException e) {
			
			e.printStackTrace();
		} catch (JsonMappingException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}

	
	public void authUsingBankId(String personalNumber) throws IllegalStateException

	{
		//Creating Headers
		HttpHeaders headers= createHeadersForRequest();
		
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
		
		//Get Headers from this API, to use it in next API Calling
		HttpHeaders responseHeaderForAuthWithBankId = response.getHeaders();
		
	
		//Calling the API in every 10 sec
		 Timer timer = new Timer();
	        timer.schedule(new TimerTask() {
	        	String authStatus=null;
	            @Override
	            public void run() {
	                System.out.println("\nRunning: " + new java.util.Date());
	                 try {
						authStatus=getAuthenticationStatus(responseHeaderForAuthWithBankId );
						System.out.println("Status Code: " + authStatus);
					} catch (JsonMappingException e) {
						
						e.printStackTrace();
					} catch (JsonProcessingException e) {
						
						e.printStackTrace();
					} catch (IOException e) {
						
						e.printStackTrace();
					} catch (Exception e) {
						System.out.println("Your Session has expired, kindly relogin ");
						System.exit(0);
						//e.printStackTrace();
					}
	                
	            }
	        }, 0, 10000);
	   
	
		
	}


	private String  getAuthenticationStatus(HttpHeaders responseHeader) throws Exception, IOException
	{
		HttpHeaders headers= createHeadersForRequest();
		String getJSessionIdFromCookie =getJSessionIdFromCookie(responseHeader);
		
		headers.set("Cookie", getJSessionIdFromCookie);
		HttpEntity entityWithJsessionId = new HttpEntity(headers);
		
		//Image Api invoking
		ResponseEntity<String> responseForBarCodeImage = restTemplate.exchange(BarcodeApi, HttpMethod.GET, entityWithJsessionId, String.class);
		String responseForBarCodeImageInString = responseForBarCodeImage.getBody();
		System.out.println("BarCode to SCAN in String " + responseForBarCodeImageInString);

		//Verify Api invoking
		ResponseEntity<String> responseForVerifyApi = restTemplate.exchange(VerifyApi, HttpMethod.GET, entityWithJsessionId, new ParameterizedTypeReference<String>() {
        });
		String responseForVerifyApiInString = responseForVerifyApi.getBody();
		

		// Deserialization into the `VerifyDto` class
		ObjectMapper objectMapper = new ObjectMapper();
		VerifyDto jsonResponse = objectMapper.readValue(responseForVerifyApiInString, VerifyDto.class);
		
		//System.out.println("Response For Verify API " + jsonResponse.getStatus());
		return jsonResponse.getStatus();

	}
	
	
	public String getJSessionIdFromCookie(HttpHeaders responseHeaderForAuthWithBankId) {
		List<String> valueForCookieKey = responseHeaderForAuthWithBankId.get("Set-cookie");
		String jsessionid = null;
		for (String values : valueForCookieKey) {
			//Get the Cookie values of JSESSION id, as this is the only cookie that is needed for authentication in further API's. 
			//Rest all other cookie values are not needed
			if (values.contains("JSESSIONID")) {
				String cookie1 = values;
				jsessionid = cookie1.split(";")[0];
				//System.out.println("Jsession Id " + jsessionid);
				
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
