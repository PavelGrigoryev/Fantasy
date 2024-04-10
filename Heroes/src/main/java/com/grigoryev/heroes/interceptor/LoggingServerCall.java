package com.grigoryev.heroes.interceptor;

import io.grpc.ForwardingServerCall;
import io.grpc.ServerCall;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingServerCall<ReqT, RespT> extends ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT> {

    protected LoggingServerCall(ServerCall<ReqT, RespT> delegate) {
        super(delegate);
    }

    @Override
    public void sendMessage(RespT responseMessage) {
        log.info("gRPC response:\n{}", responseMessage);
        super.sendMessage(responseMessage);
    }

}
