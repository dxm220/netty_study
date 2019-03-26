package pers.dxm.grpc.logiccode;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import pers.dxm.grpc.generatecode.*;

import java.time.LocalDateTime;
import java.util.Iterator;

/**
 * Created by douxm on 2019\3\25 0025.
 */
public class GrpcClient {
    public static void main(String[] args) {
        // usePlaintext：采用非加密的方式与服务器进行交互
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 8899).usePlaintext().build();
        // stub是交互的桩，ServiceBlockingStub是同步的方式，ServiceStub是异步的方式
        StudentServiceGrpc.StudentServiceBlockingStub blockingStub = StudentServiceGrpc.newBlockingStub(managedChannel);
        StudentServiceGrpc.StudentServiceStub stub = StudentServiceGrpc.newStub(managedChannel);
        // 初始化response对象
        MyResponse myResponse = blockingStub.getRealNameByUsername(MyRequest.newBuilder().setUsername("zhangsan").build());
        System.out.println(myResponse.getRealname());
        System.out.println("双端普通---------------");
        // 客户端普通服务端流式可以使用同步桩调用，返回值为response迭代集合
        Iterator<StudentResponse> iter = blockingStub.getStudentsByAge(StudentRequest.newBuilder().setAge(20).build());
        while (iter.hasNext()) {
            StudentResponse studentResponse = iter.next();
            System.out.println(studentResponse.getName() + ", " + studentResponse.getAge() + ", " + studentResponse.getCity());
        }
        System.out.println("客户端普通服务端流式---------------");
        // 客户端流式服务端普通必须采用异步桩调用，一下代码分为两部分，第一部分是客户端接收来自服务端返回的消息，第二部分是客户端向服务端发送流式消息
        // 1.客户端接收到来自服务端的消息并做相应处理，StudentResponseList是服务端返回对象，包含了客户端发送的多个request对应的response对象
        StreamObserver<StudentResponseList> studentResponseListStreamObserver = new StreamObserver<StudentResponseList>() {
            @Override
            public void onNext(StudentResponseList value) {
                value.getStudentResponseList().forEach(studentResponse -> {
                    System.out.println(studentResponse.getName());
                    System.out.println(studentResponse.getAge());
                    System.out.println(studentResponse.getCity());
                    System.out.println("----------");
                });
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("completed!");
            }
        };

        // 2.客户端向服务端发送流式消息，采用异步的方式
        StreamObserver<StudentRequest> studentRequestStreamObserver = stub.getStudentsWrapperByAges(studentResponseListStreamObserver);
        studentRequestStreamObserver.onNext(StudentRequest.newBuilder().setAge(20).build());
        studentRequestStreamObserver.onNext(StudentRequest.newBuilder().setAge(30).build());
        studentRequestStreamObserver.onNext(StudentRequest.newBuilder().setAge(40).build());
        studentRequestStreamObserver.onNext(StudentRequest.newBuilder().setAge(50).build());
        studentRequestStreamObserver.onCompleted();
        System.out.println("客户端流式服务端普通---------------");
        // 服务端返回一个响应就处理一个响应，
        StreamObserver<StreamRequest> requestStreamObserver = stub.biTalk(new StreamObserver<StreamResponse>() {
            @Override
            public void onNext(StreamResponse value) {
                System.out.println(value.getResponseInfo());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }
        });

        // 通过循环模拟客户端流式的向服务端发送request对象
        for (int i = 0; i < 10; ++i) {
            requestStreamObserver.onNext(StreamRequest.newBuilder().setRequestInfo(LocalDateTime.now().toString()).build());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("双向流---------------");
    }
}
