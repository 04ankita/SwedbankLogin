package se.swedbank.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;

import se.swedbank.dto.authenticationMethods;
@Service
public class service {
	@Autowired
	private RestTemplate restTemplate;
	
	public String getAuthenticationMethods()

	{
String url="https://online.swedbank.se/TDE_DAP_Portal_REST_WEB/api/v5/identification/";
		//String hostname="online.swedbank.se";
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Client", "fdp-internet-bank/180.1.0");
		headers.set("Authorization","QjdkWkhRY1k3OFZSVno5bDoxNjI0MDQxMzA2Mzg2");
		//	headers.set("Host","online.swedbank.se");
		headers.set("Accept", "application/json");
		headers.set( "Content-Type", "application/json");
//		HttpsURLConnection.setDefaultHostnameVerifier(ne
	
		//headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		HttpEntity entity = new HttpEntity(headers);
		ResponseEntity<String> response=restTemplate.exchange(url, HttpMethod.GET,entity,  String.class);
		//ResponseEntity<authenticationMethods> responseINJson=restTemplate.exchange(url, HttpMethod.GET,entity,  authenticationMethods.class);
		
		String respe=response.getBody();
		
		System.out.println("Auth methods : " + respe);
				
		return respe;
	}


public String authUsingBankId(String personalNumber)

{
String url="https://online.swedbank.se/TDE_DAP_Portal_REST_WEB/api/v5/identification/bankid/mobile";
	//String hostname="online.swedbank.se";
	HttpHeaders headers = new HttpHeaders();
	headers.set("X-Client", "fdp-internet-bank/180.1.0");
	headers.set("Authorization","QjdkWkhRY1k3OFZSVno5bDoxNjI0ODA3MzUzMTQy");
	//	headers.set("Host","online.swedbank.se");
	headers.set("Accept", "application/json");
	headers.set( "Content-Type", "application/json");
//	HttpsURLConnection.setDefaultHostnameVerifier(ne

	JSONObject request = new JSONObject();
	try {
		request.put("bankIdOnSameDevice", "false");
		request.put("generateEasyLoginId", "false");
		request.put("useEasyLogin", "false");
		request.put("userId", personalNumber);
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	//headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	HttpEntity entity = new HttpEntity(request.toString(),headers);
	ResponseEntity<String> response=restTemplate.exchange(url, HttpMethod.POST,entity,  String.class);
	//ResponseEntity<authenticationMethods> responseINJson=restTemplate.exchange(url, HttpMethod.GET,entity,  authenticationMethods.class);
	
	String respe=response.getBody();
	HttpHeaders CookieJsessionId=response.getHeaders();
	
	//CookieStore cookieStore = new BasicCookieStore();
//    BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", pageContext.getSession().getId());
  //  cookieStore.addCookie(cookie);
	
    
	//CookieJsessionId.get
	//headers.set("Cookie", CookieJsessionId.toString());
	List<String> keys = CookieJsessionId.get("Set-cookie");
	
	//String cookie=keys.get("Set-cookie");
	HashMap<String, String> hash = new HashMap<String, String>();
	String jsessionid=null;
    for (String values : keys) {
        if (values.contains("JSESSIONID")) {
            String cookie1 = values;
            System.out.println("Cookie: " + cookie1);
            jsessionid = cookie1.split(";")[0];
            System.out.println("Cookie: " + jsessionid);
        }
    }
   
    //cookie.get()
   // String jsessionid = cookie.split(";")[0];
   //headers.set("Cookie", cookie.l);*/
//	String set_cookie = CookieJsessionId.get
  //String set_cookie = HttpHeaders.SET_COOKIE;
  
	
	headers.set("Cookie",jsessionid);
			
	String barcodeUrl="https://online.swedbank.se/TDE_DAP_Portal_REST_WEB/api/v5/identification/bankid/mobile/image";
	HttpEntity entity1 = new HttpEntity(headers);
	ResponseEntity<String> response1=restTemplate.exchange(barcodeUrl, HttpMethod.GET,entity1,  String.class);
	String respe1=response1.getBody();
	System.out.println("Auth methods : " + respe1);
	
	return respe;
}
}




