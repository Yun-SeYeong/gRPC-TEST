# gRPC

> gRPC는 protocol buffer 또는 Interface Definition Language 로 사용할 수 있다. 



> **protocol buffer**: 프로토콜 버퍼는 구조화된 데이터를 직렬화하는 방식이다. 유선이나 데이터 저장을 목적으로 서로 통신할 프로그램을 개발할 때 유용하다.
>
> **Interface Definition Language**: 인터페이스 정의 언어는 소프트웨어 컴포넌트의 인터페이스를 묘사하기 위한 명세 언어이다. IDL은 어느 한 언어에 국한되지 않는 언어중립적인 방법으로 인터페이스를 표현함으로써, 같은 언어를 사용하지 않는 소프트웨어 컴포넌트 사이의 통신을 가능하게 한다.



### Overview

gRPC는 다른 기기, 다른 server application 사이에서 method를 바로 호출할 수 있게 해주고, application과 service 배포를 쉽게 할 수 있다. 

메소드의 파라미터와 리턴타입을 정의해서 원격으로 호출 하게된다. 서버에서는 gRPC 서버를 통해 클라이언트의 call을 핸들링한다. 클라이언트에서는 서버와 같은 메소드가 정의된 stub가 있어야한다.



