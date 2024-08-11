package br.edu.ufabc.mfmachado.humblemq;

import br.edu.ufabc.mfmachado.humblemq.configuration.grpc.GrpcConfiguration;
import br.edu.ufabc.mfmachado.humblemq.usecase.ChannelHandler;
import io.grpc.*;
import lombok.RequiredArgsConstructor;
import io.grpc.protobuf.services.ProtoReflectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class GrpcServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(GrpcServer.class);
    private Server server;

    private final List<BindableService> services;
    private final GrpcConfiguration grpcConfiguration;
    private final ChannelHandler channelHandler;

    public void start() throws IOException, InterruptedException {
        LOGGER.info("Starting gRPC server...");
        ServerBuilder<?> serverBuilder = Grpc.newServerBuilderForPort(grpcConfiguration.getPort(), InsecureServerCredentials.create());
        services.forEach(serverBuilder::addService);
        serverBuilder.addService(ProtoReflectionService.newInstance());
        this.server = serverBuilder.build();

        channelHandler.recoverChannels();
        server.start();
        stopServerOnApplicationShutdown();
        LOGGER.info("The gRPC server started! Listening on port {}", grpcConfiguration.getPort());

        server.awaitTermination();
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void stopServerOnApplicationShutdown() throws InterruptedException {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            try {
                GrpcServer.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("*** server shut down");
        }));
    }
}
