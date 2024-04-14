package com.grigoryev.service;

import com.grigoryev.HelloGrpc;
import com.grigoryev.HelloReply;
import com.grigoryev.HelloRequest;
import com.grigoryev.blog.FindByIdRequest;
import com.grigoryev.blog.MutinyBlogServiceGrpc;
import com.grigoryev.token.TokenCredentials;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.grpc.GrpcService;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;

@GrpcService
public class HelloGrpcService implements HelloGrpc {

    @GrpcClient("reactive-client")
    MutinyBlogServiceGrpc.MutinyBlogServiceStub reactiveClient;

    @Override
    public Uni<HelloReply> sayHello(HelloRequest request) {
        return reactiveClient.withCallCredentials(new TokenCredentials("Bearer access-token:clientId:+1"))
                .findById(FindByIdRequest.newBuilder().setId("1").build())
                .onItem().transform(blog -> {
                    Log.info("Received blog " + blog);
                    String message = "Hello " + request.getName() + "!";
                    return HelloReply.newBuilder().setMessage(message).build();
                })
                .log();
    }

}
