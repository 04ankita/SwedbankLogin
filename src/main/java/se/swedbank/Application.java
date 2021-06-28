package se.swedbank;



import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;



@SpringBootApplication
@ComponentScan(basePackages = {"se.swedbank"})
public class Application  implements CommandLineRunner {
	@Autowired
	private se.swedbank.service.Service servicex;
	
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args)
	{  
		SpringApplication.run(Application.class, args);  
	}
	
	@Override
	public void run(String... args) {
		// TODO Auto-generated method stub
		try {
		servicex.getAuthenticationMethods();
		System.out.println("Logging in with first Authentictaion method : Bank id for 19101010-1010 : ");
		servicex.authUsingBankId("19101010-1010");
		}
		catch (Exception e) {
			System.out.println("Session has expired ");
			System.exit(0);
		}
		
		
	} 

}