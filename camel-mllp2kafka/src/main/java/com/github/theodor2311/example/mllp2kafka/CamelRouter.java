package com.github.theodor2311.example.mllp2kafka;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CamelRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("mllp:{{camel.component.mllp.configuration.hostname}}:{{camel.component.mllp.configuration.port}}")
        .to("direct:toKafka")
                ;

        from("direct:toKafka")
                .to("kafka:{{topics}}")
                ;

    }

}