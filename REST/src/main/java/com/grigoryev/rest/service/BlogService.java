package com.grigoryev.rest.service;

import com.google.protobuf.Empty;
import com.grigoryev.blog.Blog;
import com.grigoryev.blog.BlogServiceGrpc;
import com.grigoryev.blog.FindByIdRequest;
import com.grigoryev.rest.token.TokenCredentials;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class BlogService {

    @Value("${gRPC.host}")
    private String host;

    @Value("${gRPC.port}")
    private Integer port;

    public Mono<Blog> findById(String id, String token) {
        ManagedChannel channel = getChannel();
        BlogServiceGrpc.BlogServiceStub asyncStub = BlogServiceGrpc.newStub(channel)
                .withCallCredentials(new TokenCredentials(token));
        return Mono.<Blog>create(sink -> {
                    StreamObserver<Blog> responseObserver = new StreamObserver<>() {
                        @Override
                        public void onNext(Blog value) {
                            sink.success(value);
                        }

                        @Override
                        public void onError(Throwable t) {
                            sink.error(t);
                        }

                        @Override
                        public void onCompleted() {
                            channel.shutdown();
                        }
                    };
                    asyncStub.findById(FindByIdRequest.newBuilder().setId(id).build(), responseObserver);
                })
                .log();
    }

    public Flux<Blog> findAll(String token) {
        ManagedChannel channel = getChannel();
        BlogServiceGrpc.BlogServiceStub asyncStub = BlogServiceGrpc.newStub(channel)
                .withCallCredentials(new TokenCredentials(token));
        return Flux.<Blog>create(sink -> {
                    StreamObserver<Blog> responseObserver = new StreamObserver<>() {
                        @Override
                        public void onNext(Blog blog) {
                            sink.next(blog);
                        }

                        @Override
                        public void onError(Throwable t) {
                            sink.error(t);
                        }

                        @Override
                        public void onCompleted() {
                            sink.complete();
                            channel.shutdown();
                        }
                    };
                    asyncStub.findAll(Empty.newBuilder().build(), responseObserver);
                })
                .delayElements(Duration.ofSeconds(2))
                .log();
    }

    private ManagedChannel getChannel() {
        return ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
    }

}
