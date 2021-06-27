package se.swedbank;

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
	private se.swedbank.service.service servicex;
	
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args)
	{  
	SpringApplication.run(Application.class, args);  
	}
@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		servicex.getAuthenticationMethods();
		System.out.println("Logging in with Bank id");
		servicex.authUsingBankId("19920404-5448");
		System.exit(0);
		
	} 

}