package com.grigoryev.heroes.interceptor;

import io.grpc.ForwardingServerCallListener;
import io.grpc.ServerCall;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingServerCallListener<ReqT> extends ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT> {

    protected LoggingServerCallListener(ServerCall.Listener<ReqT> delegate) {
        super(delegate);
    }

    @Override
    public void onMessage(ReqT message) {
        log.info("gRPC request:\n{}", message);
        super.onMessage(message);
    }

    @Override
    public void onHalfClose() {
        log.info("Client has finished sending messages");
        super.onHalfClose();
    }

}
