package com.saber.spring_camel_mongodb_demo.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ObjectMapper mapper(){
        return new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false)
                .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE,false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                ;
    }

    @Bean(value = "mongoBean")
    public MongoClient mongoClient(@Value("${spring.data.mongodb.username}") String username,
                                   @Value("${spring.data.mongodb.password}")    String password,
                                   @Value("${spring.data.mongodb.port}")    Integer port,
                                   @Value("${spring.data.mongodb.dataBase}")    String dataBase){
        return MongoClients.create(String.format("mongodb://%s:%s@localhost:%s/%s",username,password,port,dataBase ));
    }
}
