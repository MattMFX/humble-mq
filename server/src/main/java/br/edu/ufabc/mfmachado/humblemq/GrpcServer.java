package br.edu.ufabc.mfmachado.humblemq;

import br.edu.ufabc.mfmachado.humblemq.configuration.grpc.GrpcConfiguration;
import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class GrpcServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(GrpcServer.class);
    private Server server;

    private final BindableService createChannelServiceImpl;
    private final BindableService listChannelServiceImpl;
    private final GrpcConfiguration grpcConfiguration;

    public GrpcServer(BindableService createChannelServiceImpl, BindableService listChannelServiceImpl, GrpcConfiguration grpcConfiguration) {
        this.createChannelServiceImpl = createChannelServiceImpl;
        this.listChannelServiceImpl = listChannelServiceImpl;
        this.grpcConfiguration = grpcConfiguration;
    }

    public void start() throws IOException, InterruptedException {
        LOGGER.info("Starting gRPC server...");
        Server server = Grpc.newServerBuilderForPort(grpcConfiguration.getPort(), InsecureServerCredentials.create())
                .addService(createChannelServiceImpl)
                .addService(listChannelServiceImpl)
                .build();
        this.server = server;

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
