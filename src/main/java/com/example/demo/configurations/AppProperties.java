package com.example.demo.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;

@Data
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private Path imagesPath;
    private String imagesEndpoint;
    private String customersEndpoint;
    private String usersEndpoint;

}
