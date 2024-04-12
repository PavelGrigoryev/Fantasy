package com.grigoryev.rest.token;

import io.grpc.CallCredentials;
import io.grpc.Metadata;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Executor;

@RequiredArgsConstructor
public class TokenCredentials extends CallCredentials {

    private final String token;

    @Override
    public void applyRequestMetadata(RequestInfo requestInfo, Executor executor, MetadataApplier metadataApplier) {
        executor.execute(() -> {
            Metadata headers = new Metadata();
            Metadata.Key<String> key = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
            headers.put(key, token != null ? token : "");
            metadataApplier.apply(headers);
        });
    }

}
