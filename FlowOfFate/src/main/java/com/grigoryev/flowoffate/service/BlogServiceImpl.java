package com.grigoryev.flowoffate.service;

import com.google.protobuf.Empty;
import com.grigoryev.blog.Blog;
import com.grigoryev.blog.BlogServiceGrpc;
import com.grigoryev.blog.FindByIdRequest;
import com.grigoryev.flowoffate.mapper.BlogMapper;
import com.grigoryev.flowoffate.repository.BlogRepository;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.mapstruct.factory.Mappers;

public class BlogServiceImpl extends BlogServiceGrpc.BlogServiceImplBase {

    private final BlogRepository blogRepository;
    private final BlogMapper blogMapper;

    public BlogServiceImpl() {
        blogRepository = new BlogRepository();
        blogMapper = Mappers.getMapper(BlogMapper.class);
    }

    @Override
    public void findById(FindByIdRequest request, StreamObserver<Blog> responseObserver) {
        Blog blog = blogRepository.findById(request.getId())
                .map(blogMapper::toBlog)
                .orElseThrow(() -> {
                    StatusRuntimeException runtimeException = Status.NOT_FOUND
                            .withDescription("Blog with id %s not found".formatted(request.getId()))
                            .asRuntimeException();
                    responseObserver.onError(runtimeException);
                    return runtimeException;
                });
        responseObserver.onNext(blog);
        responseObserver.onCompleted();
    }

    @Override
    public void findAll(Empty request, StreamObserver<Blog> responseObserver) {
        blogRepository.findAll()
                .stream()
                .map(blogMapper::toBlog)
                .forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }

}
