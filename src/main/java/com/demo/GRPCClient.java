package com.demo;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class GRPCClient {
    private static HelloServiceGrpc.HelloServiceBlockingStub blockingStub;

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8800").usePlaintext().build();

        blockingStub = HelloServiceGrpc.newBlockingStub(channel);

        HelloRequest request = HelloRequest.newBuilder()
                .setFirstName("firstnameEx")
                .setLastName("lastNameEx")
                .build();

        HelloResponse response;
        try {
            response = blockingStub.hello(request);
        } catch (StatusRuntimeException e) {
            return;
        }
        System.out.println("Greeting: " + response.getGreeting());
    }
}
