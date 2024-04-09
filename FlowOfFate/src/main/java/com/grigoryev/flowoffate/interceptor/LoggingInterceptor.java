package com.grigoryev.flowoffate.interceptor;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
                                                                 Metadata metadata,
                                                                 ServerCallHandler<ReqT, RespT> serverCallHandler) {
        log.info("gRPC call: {}", serverCall.getMethodDescriptor().getFullMethodName());

        ServerCall<ReqT, RespT> loggingServerCall = new ForwardingServerCall.SimpleForwardingServerCall<>(serverCall) {
            @Override
            public void sendMessage(RespT responseMessage) {
                log.info("gRPC response:\n{}", responseMessage);
                super.sendMessage(responseMessage);
            }
        };

        ServerCall.Listener<ReqT> listener = serverCallHandler.startCall(loggingServerCall, metadata);

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(listener) {
            @Override
            public void onMessage(ReqT message) {
                log.info("gRPC request:\n{}", message);
                super.onMessage(message);
            }
        };
    }

}
