package com.github.theodor2311.example.kinesis2kafka;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.kinesis.AmazonKinesisClient;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.amazonaws.auth.AWSStaticCredentialsProvider;

@SpringBootApplication
public class Application {

    @Value("${camel.component.aws-kinesis.configuration.access-key}")
    private String access_key;
    @Value("${camel.component.aws-kinesis.configuration.secret-key}")
    private String secret_key;
    @Value("${endpoint}")
    private String endpoint;
    @Value("${camel.component.aws-kinesis.region}")
    private String awsRegion;

    @Bean("kinesisClient")
    public AmazonKinesisClient client() {
        AmazonKinesisClient client = (AmazonKinesisClient) AmazonKinesisClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint,awsRegion))
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(access_key, secret_key))).build();
        return client;
    }

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

}
