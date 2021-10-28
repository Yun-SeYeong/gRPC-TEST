package com.demo;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GRPCServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder
                .forPort(8800)
                .addService(new HelloServiceImpl())
                .build();

        System.out.println("server staring...");

        server.start();
        server.awaitTermination();
    }
}
