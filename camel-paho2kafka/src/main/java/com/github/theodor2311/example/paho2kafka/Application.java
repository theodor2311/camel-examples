package com.github.theodor2311.example.paho2kafka;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    @Value("${camel.component.paho.serverURIs}")
    private String[] serverURIs;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean("mqtt_options")
    public MqttConnectOptions mqttConnectOptions(){
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setServerURIs (serverURIs);
        return mqttConnectOptions;
    }

}