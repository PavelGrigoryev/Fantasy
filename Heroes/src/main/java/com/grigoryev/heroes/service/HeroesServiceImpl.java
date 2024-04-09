package com.grigoryev.heroes.service;

import com.grigoryev.heroes.FindByIdRequest;
import com.grigoryev.heroes.Hero;
import com.grigoryev.heroes.HeroStatus;
import com.grigoryev.heroes.HeroType;
import com.grigoryev.heroes.HeroesServiceGrpc;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

public class HeroesServiceImpl extends HeroesServiceGrpc.HeroesServiceImplBase {

    @Override
    public StreamObserver<FindByIdRequest> findById(StreamObserver<Hero> responseObserver) {
        return new StreamObserver<>() {

            @Override
            public void onNext(FindByIdRequest findByIdRequest) {
                long id = findByIdRequest.getId();
                responseObserver.onNext(Hero.newBuilder().setId(id)
                        .setName("ForeverHunter")
                        .setHeroType(HeroType.HUNTER)
                        .setHeroStatus(HeroStatus.RESTING)
                        .setLevel(80)
                        .build());
            }

            @Override
            public void onError(Throwable throwable) {
                StatusRuntimeException runtimeException = Status.NOT_FOUND
                        .withDescription("This hero is not exist")
                        .asRuntimeException();
                responseObserver.onError(runtimeException);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

}
