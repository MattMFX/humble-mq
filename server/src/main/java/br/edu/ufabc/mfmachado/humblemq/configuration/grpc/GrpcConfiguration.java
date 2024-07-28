package br.edu.ufabc.mfmachado.humblemq.configuration.grpc;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "grpc")
@Data
public class GrpcConfiguration {

        private int port;
}
