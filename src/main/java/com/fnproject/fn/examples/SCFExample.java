package com.fnproject.fn.examples;

import java.util.function.Function;
import com.fnproject.fn.api.FnFeature;
import com.fnproject.springframework.function.SpringCloudFunctionFeature;
import org.springframework.cloud.function.context.config.ContextFunctionCatalogAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ContextFunctionCatalogAutoConfiguration.class)
@FnFeature(SpringCloudFunctionFeature.class)
public class SCFExample {
    // Unused - see https://github.com/fnproject/fdk-java/issues/113
    public void handleRequest() { }

    @Bean
    public Function<String, String> function(){
        return value -> "Hello, " + ((value == null || value.isEmpty()) ? "world"  : value ) + "!";
    }
}
