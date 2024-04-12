package com.grigoryev.flowoffate.interceptor;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;

public class AuthorizationInterceptor implements ServerInterceptor {

    private static final String BEARER_TYPE = "Bearer";
    private static final String REFRESH_SUFFIX = "+1";
    private static final String ACCESS_TOKEN = "access-token";
    private static final Context.Key<String> CLIENT_ID_CONTEXT_KEY = Context.key("clientId");

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
                                                                 Metadata metadata,
                                                                 ServerCallHandler<ReqT, RespT> serverCallHandler) {
        String authToken = metadata.get(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER));
        Status status;
        if (authToken == null) {
            status = Status.UNAUTHENTICATED.withDescription("Authorization token is missing");
        } else if (!authToken.startsWith(BEARER_TYPE)) {
            status = Status.UNAUTHENTICATED.withDescription("Unknown authorization type");
        } else {
            String tokenValue = authToken.substring(BEARER_TYPE.length()).trim();
            if (!tokenValue.startsWith(ACCESS_TOKEN)) {
                status = Status.UNAUTHENTICATED.withDescription("Invalid access token authHeaderValue");
            } else {
                String[] tokens = tokenValue.split(":");
                if (tokens.length >= 3 && tokens[2].equals(REFRESH_SUFFIX)) {
                    Context ctx = Context.current()
                            .withValue(CLIENT_ID_CONTEXT_KEY, tokens[1]);
                    return Contexts.interceptCall(ctx, serverCall, metadata, serverCallHandler);
                } else {
                    status = Status.UNAUTHENTICATED.withDescription("Stale credentials");
                }
            }
        }

        serverCall.close(status, new Metadata());
        return new ServerCall.Listener<>() {
        };
    }

}
