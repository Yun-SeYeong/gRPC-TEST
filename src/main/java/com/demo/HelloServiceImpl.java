package com.demo;

import io.grpc.stub.StreamObserver;

public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {
    @Override
    public void hello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        String greeting = new StringBuilder()
                .append("hello, ")
                .append(request.getFirstName())
                .append(" ")
                .append(request.getLastName())
                .toString();

        HelloResponse helloResponse = HelloResponse.newBuilder()
                .setGreeting(greeting)
                .build();

        responseObserver.onNext(helloResponse);
        responseObserver.onCompleted();
    }
}
