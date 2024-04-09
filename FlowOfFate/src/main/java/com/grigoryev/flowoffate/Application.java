package com.grigoryev.flowoffate;

import com.grigoryev.flowoffate.service.BlogServiceImpl;
import com.grigoryev.flowoffate.util.YamlUtil;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Application {

    public static void main(String[] args) throws IOException, InterruptedException {
        log.info("Hello gRPC!");
        int port = Integer.parseInt(YamlUtil.getYaml().get("server").get("port"));

        Server server = ServerBuilder.forPort(port)
                .addService(new BlogServiceImpl())
                .build()
                .start();

        log.info("gRPC server started, listening on localhost:{}", port);

        Runtime.getRuntime()
                .addShutdownHook(new Thread(server::shutdown));

        server.awaitTermination();
    }

}
