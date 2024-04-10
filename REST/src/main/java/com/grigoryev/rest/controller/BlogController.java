package com.grigoryev.rest.controller;

import com.google.protobuf.Empty;
import com.grigoryev.blog.Blog;
import com.grigoryev.blog.BlogServiceGrpc;
import com.grigoryev.blog.FindByIdRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@RestController
@RequestMapping(value = "/blog", produces = "application/json")
public class BlogController {

    @GetMapping("/{id}")
    public Mono<Blog> findById(@PathVariable String id) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        BlogServiceGrpc.BlogServiceStub asyncStub = BlogServiceGrpc.newStub(channel);

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

    @GetMapping
    public Flux<Blog> findAll() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        BlogServiceGrpc.BlogServiceStub asyncStub = BlogServiceGrpc.newStub(channel);

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
                .delayElements(Duration.ofSeconds(3))
                .log();
    }

}
