package com.grigoryev.flowoffate;

import com.grigoryev.flowoffate.service.BlogServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Application {

    public static void main(String[] args) throws IOException, InterruptedException {
        log.info("Hello gRPC");

        Server server = ServerBuilder.forPort(50051)
                .addService(new BlogServiceImpl())
                .build()
                .start();

        Runtime.getRuntime()
                .addShutdownHook(new Thread(server::shutdown));

        server.awaitTermination();
    }

}
