package com.grigoryev.heroes.interceptor;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
                                                                 Metadata metadata,
                                                                 ServerCallHandler<ReqT, RespT> serverCallHandler) {
        log.info("gRPC call: {}", serverCall.getMethodDescriptor().getFullMethodName());
        ServerCall<ReqT, RespT> loggingServerCall = new LoggingServerCall<>(serverCall);
        ServerCall.Listener<ReqT> listener = serverCallHandler.startCall(loggingServerCall, metadata);
        return new LoggingServerCallListener<>(listener);
    }

}
