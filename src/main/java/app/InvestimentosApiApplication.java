package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class InvestimentosApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvestimentosApiApplication.class, args);
	}

}
