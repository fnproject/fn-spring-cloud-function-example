package com.fnproject.fn.examples;

import com.fnproject.fn.api.FnConfiguration;
import com.fnproject.fn.api.RuntimeContext;
import com.fnproject.springframework.function.SpringCloudFunctionInvoker;
import org.springframework.cloud.function.context.ContextFunctionCatalogAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.function.Function;

@Configuration
@Import(ContextFunctionCatalogAutoConfiguration.class)
public class SCFExample {
    @FnConfiguration
    public static void configure(RuntimeContext ctx) {
        ctx.setInvoker(new SpringCloudFunctionInvoker(SCFExample.class));
    }

    // Unused - see https://github.com/fnproject/fdk-java/issues/113
    public void handleRequest() { }

    @Bean
    public Function<String, String> upperCase(){
        return String::toUpperCase;
    }

    @Bean
    public Function<String, String> lowerCase(){
        return String::toLowerCase;
    }
}
