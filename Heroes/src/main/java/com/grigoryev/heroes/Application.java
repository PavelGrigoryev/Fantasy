package com.grigoryev.heroes;

import com.grigoryev.heroes.interceptor.LoggingInterceptor;
import com.grigoryev.heroes.service.HeroesServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Application {

    public static void main(String[] args) throws InterruptedException, IOException {
        log.info("Hello gRPC!");

        int port = 50052;
        Server server = ServerBuilder.forPort(port)
                .addService(new HeroesServiceImpl())
                .intercept(new LoggingInterceptor())
                .build()
                .start();

        log.info("gRPC server started, listening on localhost:{}", port);

        Runtime.getRuntime()
                .addShutdownHook(new Thread(server::shutdown));

        server.awaitTermination();
    }

}