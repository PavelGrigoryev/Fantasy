package com.grigoryev.flowoffate.service;

import com.google.protobuf.Empty;
import com.grigoryev.blog.Blog;
import com.grigoryev.blog.BlogServiceGrpc;
import com.grigoryev.flowoffate.mapper.BlogMapper;
import com.grigoryev.flowoffate.repository.BlogRepository;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;

@Slf4j
public class BlogServiceImpl extends BlogServiceGrpc.BlogServiceImplBase {

    private final BlogRepository blogRepository;
    private final BlogMapper blogMapper;

    public BlogServiceImpl() {
        blogRepository = new BlogRepository();
        blogMapper = Mappers.getMapper(BlogMapper.class);
    }

    @Override
    public void findAll(Empty request, StreamObserver<Blog> responseObserver) {
        blogRepository.findAll()
                .stream()
                .map(blogMapper::toBlog)
                .forEach(blog -> {
                    log.info("Found blog: {}", blog);
                    responseObserver.onNext(blog);
                });
        responseObserver.onCompleted();
    }

}
