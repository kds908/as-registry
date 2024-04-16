package cn.abner.asregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ASRegistryConfigProperties.class)
public class AsRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsRegistryApplication.class, args);
    }

}
