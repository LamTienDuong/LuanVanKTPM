package vn.luanvan.ktpm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

//disable security
//@SpringBootApplication(exclude = {
//		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
//		org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class
//})

@SpringBootApplication
@EnableAsync
public class LuanvanApplication {

	public static void main(String[] args) {
		SpringApplication.run(LuanvanApplication.class, args);
	}

}
