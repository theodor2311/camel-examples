/**
 *  Copyright 2005-2016 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package com.redhat.fuse.kinesis2kafka;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.apache.camel.processor.idempotent.kafka.KafkaIdempotentRepository;
import org.apache.camel.processor.idempotent.FileIdempotentRepository;
import java.io.File;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;


/**
 * Kinesis route
 */

@Component
public class CamelRouter extends RouteBuilder {

    @Value("${idempotent-topic}")
    private String idempotentTopic;
    @Value("${camel.component.kafka.configuration.brokers}")
    private String brokers;
    @Value("${camel.component.kafka.configuration.security-protocol}")
    private String securityProtocol;
    @Value("${camel.component.kafka.configuration.ssl-truststore-location}")
    private String sslTruststoreLocation;
    @Value("${camel.component.kafka.configuration.ssl-truststore-password}")
    private String sslTruststorePassword;
    @Value("${camel.component.kafka.configuration.sasl-mechanism}")
    private String saslMechanism;
    @Value("${camel.component.kafka.configuration.sasl-jaas-config}")
    private String saslJaasConfig;

    @Override
    public void configure() throws Exception {

        Properties settings = new Properties();
        settings.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG,brokers);
        settings.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,securityProtocol);
        settings.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,sslTruststoreLocation);
        settings.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,sslTruststorePassword);
        settings.put(SaslConfigs.SASL_MECHANISM,saslMechanism);
        settings.put(SaslConfigs.SASL_JAAS_CONFIG,saslJaasConfig);

        from("aws-kinesis:{{camel.component.aws-kinesis.configuration.stream-name}}")
//                .idempotentConsumer(header("CamelAwsKinesisSequenceNumber"),new KafkaIdempotentRepository(idempotentTopic,settings,settings))
        .to("direct:toKafka")
                ;

        from("direct:toKafka")
                .to("kafka:{{topics}}")
                ;


    }

}