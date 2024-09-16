package vn.luanvan.ktpm.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;


@Service
public class DatabaseInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> START INIT DATABASE");

        System.out.println(">>> END INIT");
    }
}
