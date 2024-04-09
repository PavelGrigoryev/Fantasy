package com.grigoryev.heroes;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {

    public static void main(String[] args) throws InterruptedException {
        log.info("Hello gRPC!");

        int port = 50052;
        Server server = ServerBuilder.forPort(port)
                .addService(new HeroServiceImpl())
                .build()
                .start();

        log.info("gRPC server started, listening on localhost:{}", port);

        Runtime.getRuntime()
                .addShutdownHook(new Thread(server::shutdown));

        server.awaitTermination();
    }

}