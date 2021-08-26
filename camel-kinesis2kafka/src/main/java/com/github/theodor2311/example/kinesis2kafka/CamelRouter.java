package com.github.theodor2311.example.kinesis2kafka;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.idempotent.kafka.KafkaIdempotentRepository;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Properties;

@Component
public class CamelRouter extends RouteBuilder {

    @Value("${idempotent-topic}")
    private String idempotentTopic;
    @Value("${camel.component.kafka.configuration.brokers}")
    private String brokers;
    @Value("${camel.component.kafka.configuration.security-protocol}")
    private String securityProtocol;
    @Value("${camel.component.kafka.configuration.ssl-truststore-location:#{null}}")
    private String sslTruststoreLocation;
    @Value("${camel.component.kafka.configuration.ssl-truststore-password:#{null}}")
    private String sslTruststorePassword;
    @Value("${camel.component.kafka.configuration.sasl-mechanism:#{null}}")
    private String saslMechanism;
    @Value("${camel.component.kafka.configuration.sasl-jaas-config:#{null}}")
    private String saslJaasConfig;

    @Override
    public void configure() throws Exception {

        Properties settings = new Properties();
        settings.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG,brokers);
        settings.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,securityProtocol);

        if(sslTruststoreLocation != null && !sslTruststoreLocation.isEmpty()) {
            settings.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,sslTruststoreLocation);
        }

        if(sslTruststorePassword != null && !sslTruststorePassword.isEmpty()) {
            settings.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,sslTruststorePassword);
        }

        if(saslMechanism != null && !saslMechanism.isEmpty()) {
            settings.put(SaslConfigs.SASL_MECHANISM,saslMechanism);
        }

        if(saslJaasConfig != null && !saslJaasConfig.isEmpty()) {
            settings.put(SaslConfigs.SASL_JAAS_CONFIG,saslJaasConfig);
        }

        from("aws-kinesis:{{camel.component.aws-kinesis.configuration.stream-name}}?amazonKinesisClient=#kinesisClient")
                 .idempotentConsumer(header("CamelAwsKinesisSequenceNumber"),new KafkaIdempotentRepository(idempotentTopic,settings,settings))
        .to("direct:toKafka")
                ;

        from("direct:toKafka")
                .to("kafka:{{topics}}")
                ;

    }

}