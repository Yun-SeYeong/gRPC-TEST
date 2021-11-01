# gRPC

> gRPC는 protocol buffer 또는 Interface Definition Language 로 사용할 수 있다. 



> **protocol buffer**: 프로토콜 버퍼는 구조화된 데이터를 직렬화하는 방식이다. 유선이나 데이터 저장을 목적으로 서로 통신할 프로그램을 개발할 때 유용하다.
>
> **Interface Definition Language**: 인터페이스 정의 언어는 소프트웨어 컴포넌트의 인터페이스를 묘사하기 위한 명세 언어이다. IDL은 어느 한 언어에 국한되지 않는 언어중립적인 방법으로 인터페이스를 표현함으로써, 같은 언어를 사용하지 않는 소프트웨어 컴포넌트 사이의 통신을 가능하게 한다.



### Overview

gRPC는 다른 기기, 다른 server application 사이에서 method를 바로 호출할 수 있게 해주고, application과 service 배포를 쉽게 할 수 있다. 메소드의 파라미터와 리턴타입을 정의해서 원격으로 호출 하게된다. 서버에서는 gRPC 서버를 통해 클라이언트의 call을 핸들링한다. 클라이언트에서는 서버와 같은 메소드가 정의된 stub가 있어야한다.



![landing-2](https://grpc.io/img/landing-2.svg)

gRPC Client들은 다양한 환경에서 서버와 통신 할 수 있다. gRPC는 여러 언어들을 지원하는 데 자바로 된 서버에 go, python, ruby등 언어로 클라이언트를 구성할 수 있다. 또한 Google API도 gRPC를 지원해서 구글의 다양한 기능을 사용할 수 있다.







### gRPC 메소드 형태

> 총 4가지 방법으로 service method를 정의 할 수 있다.



1. 가장 일반적이 방법으로 Client에서 하나의 request를 요청하면 Server에서 하나의 response를 전달하는 방식이다.

   ```
   rpc SayHello(HelloRequest) returns (HelloResponse);
   ```



2. Client가 서버로 request를 보내면 연속적인 데이터인 stream으로 응답한다. 클라이언트는 더 이상 메시지가 없을 때까지 읽는다. 또한 각각의 gRPC 메시지 순서를 보장한다.

   ```
   rpc LotsOfReplies(HelloRequest) returns (stream HelloResponse);
   ```

   

3. 클라이언트는 연속적인 데이터인 stream형태로 데이터를  전송한다. 클라이언트는 메시지 전송이 완료되면 서버로부터 response를 기다린다. 이 메소드형태 또한 각각의 gRPC메시지 순서를 보장한다.

   ```
   rpc LotsOfGreetings(stream HelloRequest) returns (HelloResponse);
   ```



4. 양방향 gRPC 스트리밍은 서로에게 read-write stream을 통해 연속적인 메시지를 보낸다.  두 스트림은 서로 독립적으로 동작하고, 따라서 서로 순서에 상관없이 서로 읽고 쓸 수 있다. 예를 들면 서버에서 모든 매시지를 받은 때까지 기다리렸다가 response를 전달할 필요가 없다. 각각의 스트림은 서로 순서가 섞이지 않는다.

   ```
   rpc BidiHello(stream HelloRequest) returns (stream HelloResponse);
   ```







### API 사용하기

> gRPC는 protocol buffer compiler plugin을 이용하여 정의된 .proto 파일을 client와 서버에 code를 생성해 준다. gRPC 유저는 클라이언트에서 생성된 API들을 호출하고 서버에서 해당하는 API를 실행하게된다.



- 서버에서는 정의된 서비스에 따라 method들을 실행하게 되고 client의 요청을 handling하게 된다. gRPC 환경의 구조에서는 들어오는 request를 decode하고 해당하는 method를 실행하고 response를 encode하게 된다.
- 클라이언트에서는 stub을 통해 실행시킬 서비스의 method를 알 수 있다. 클라이언트는 로컬의 오브젝트를 통해 method를 호출하기만 하면되고 request와 response를 적절한 protocol buffer message 타입으로 변환하게 된다. 





### Synchronous vs. asynchronous

> RPC를 동기적으로 처리하면 서버로부터 response를 응답 받을 때 까지 block이 된고, 이는 RPC 메시지가 순차적으로 들어오길 바라는 것에 가깝다. 하지만 nework처리는 본질적으로 비동기이고, 그런 시나리오가 많기 때문에 활용도 있게 사용하려면 사용중인 thread를 blocking하지 않는게 좋다.







### 세팅방법

> 자바로 세팅된 gradle 프로젝트에 gRPC Server와 gRPC Client 구현



1. 다음과 같이 build.gradle 파일 수정

   build.gradle

   ```groovy
   plugins {
       id 'com.google.protobuf' version '0.8.17' // 추가
       id 'java'
   }
   
   group 'org.example'
   version '1.0-SNAPSHOT'
   
   repositories {
       mavenCentral()
   }
   
   dependencies {
       // ===== 추가 =====
       implementation 'io.grpc:grpc-netty-shaded:1.41.0'
       implementation 'io.grpc:grpc-protobuf:1.41.0'
       implementation 'io.grpc:grpc-stub:1.41.0'
       compileOnly 'org.apache.tomcat:annotations-api:6.0.53'
       // ===============
     
       testImplementation 'org.junit.jupiter:junit-jupiter-api:5.7.0'
       testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.7.0'
   }
   
   test {
       useJUnitPlatform()
   }
   
   // 추가
   // generageProto 등 유틸제공
   protobuf {
       protoc {
           artifact = "com.google.protobuf:protoc:3.17.3"
       }
       plugins {
           grpc {
               artifact = 'io.grpc:protoc-gen-grpc-java:1.41.0'
           }
       }
       generateProtoTasks {
           all()*.plugins {
               grpc {}
           }
       }
   }
   ```

   

2. HelloService.proto 작성

   ```proto
   // 사용될 문법
   syntax = "proto3";
   // 기본적으로 생성될 자바파일이 하나로 되어있으나 true로 설저하면 하나하나 생성된다.
   option java_multiple_files = true;
   // 생성할 패키지
   package com.demo;
   
   // request message 형태 정의
   message HelloRequest {
     string firstName = 1;
     string lastName = 2;
   }
   
   // response message 형태 정의
   message HelloResponse {
     string greeting = 1;
   }
   
   // rpc method 정의
   service HelloService {
     rpc hello(HelloRequest) returns (HelloResponse);
   }
   ```

   

3. generate Proto를 통해 코드 생성

   <img src="doc/image1.png" alt="image1" height="300" />

   

   | option java_multiple_files = false                        | option java_multiple_files = true                         |
   | --------------------------------------------------------- | --------------------------------------------------------- |
   | <img src="./doc/image2.png" alt="image1" style="zoom:50%;" /> | <img src="./doc/image3.png" alt="image1" style="zoom:50%;" /> |

   

4. 생성된 HelloServiceGrpc.java를 보면 proto에 정의한 method가 추상클래가 생긴다.

   ```java
   /**
    */
   public static abstract class HelloServiceImplBase implements io.grpc.BindableService {
   
     /**
      */
     public void hello(com.demo.HelloRequest request,
         io.grpc.stub.StreamObserver<com.demo.HelloResponse> responseObserver) {
       io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getHelloMethod(), responseObserver);
     }
   
     @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
       return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
           .addMethod(
             getHelloMethod(),
             io.grpc.stub.ServerCalls.asyncUnaryCall(
               new MethodHandlers<
                 com.demo.HelloRequest,
                 com.demo.HelloResponse>(
                   this, METHODID_HELLO)))
           .build();
     }
   }
   ```

   

5. 위에 생성된 클래스 구현

   ```java
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
   ```



6. 위에 구현한 service를 서버에 등록한다.

   ```java
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
   ```



7. Client 구현

   ```java
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
   ```



8. 테스트 결과

   1. 서버

      ```
      11:42:40 오전: Executing task ':GRPCServer.main()'...
      
      > Task :extractIncludeProto UP-TO-DATE
      > Task :extractProto UP-TO-DATE
      > Task :generateProto UP-TO-DATE
      > Task :compileJava
      > Task :processResources UP-TO-DATE
      > Task :classes
      
      > Task :GRPCServer.main()
      server staring...
      ```

   2. 클라이언트

      ```
      3:59:44 오후: Executing task ':GRPCClient.main()'...
      
      > Task :extractIncludeProto UP-TO-DATE
      > Task :extractProto UP-TO-DATE
      > Task :generateProto UP-TO-DATE
      > Task :compileJava
      > Task :processResources
      > Task :classes
      
      > Task :GRPCClient.main()
      Greeting: hello, firstnameEx lastNameEx
      
      Deprecated Gradle features were used in this build, making it incompatible with Gradle 8.0.
      
      You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.
      
      See https://docs.gradle.org/7.1/userguide/command_line_interface.html#sec:command_line_warnings
      
      BUILD SUCCESSFUL in 4s
      6 actionable tasks: 3 executed, 3 up-to-date
      3:59:49 오후: Task execution finished ':GRPCClient.main()'.
      ```







