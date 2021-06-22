package com.github.theodor2311.example.paho2kafka;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CamelRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {


        from("paho:{{camel.component.paho.topic}}?qos={{camel.component.paho.qos}}&userName={{camel.component.paho.userName}}&password={{camel.component.paho.password}}")
                .to("direct:toKafka")
                ;

        from("direct:toKafka")
                .to("kafka:{{topics}}")
                ;


    }

}