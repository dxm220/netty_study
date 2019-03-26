package pers.dxm.grpc.logiccode;

import io.grpc.stub.StreamObserver;
import pers.dxm.grpc.generatecode.*;

import java.util.UUID;

/**
 * Created by douxm on 2019\3\25 0025.
 */
public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase {
    /**
     * 客户端发送与服务端返回均为普通参数
     */
    @Override
    public void getRealNameByUsername(MyRequest request, StreamObserver<MyResponse> responseObserver) {
        // 接收到来自客户端的消息
        System.out.println("接受到客户端信息： " + request.getUsername());
        // 对客户端消息进行逻辑处理，而后返回，onNext即处理过程调用的函数（每执行一步操作就可以调用一遍onNext方法），onCompleted即处理结束后调用的函数
        responseObserver.onNext(MyResponse.newBuilder().setRealname("张三").build());
        responseObserver.onCompleted();
    }

    /**
     * 客户端发送普通参数，服务端返回流式参数
     */
    @Override
    public void getStudentsByAge(StudentRequest request, StreamObserver<StudentResponse> responseObserver) {
        // 接收到来自客户端的消息
        System.out.println("接受到客户端信息：" + request.getAge());
        // 服务端进行处理，封装相应参数返回给客户端，由于流式参数的原因，可以返回多个response对象
        responseObserver.onNext(StudentResponse.newBuilder().setName("张三").setAge(20).setCity("北京").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("李四").setAge(30).setCity("天津").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("王五").setAge(40).setCity("成都").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("赵六").setAge(50).setCity("深圳").build());
        responseObserver.onCompleted();
    }

    /**
     * 客户端发送流式参数，服务端返回普通参数（以上是场景，与方法真正的传参返回值不一致，好好理解）
     */
    @Override
    public StreamObserver<StudentRequest> getStudentsWrapperByAges(StreamObserver<StudentResponseList> responseObserver) {
        // 来一个请求就处理一次
        return new StreamObserver<StudentRequest>() {
            // 接收到流中的一个请求对象，就调用一次onNext方法进行处理
            @Override
            public void onNext(StudentRequest value) {
                System.out.println("onNext: " + value.getAge());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            // 当流中所有的请求都处理完成后，调用onCompleted方法对请求的返回参数进行封装，封装成一个responseList对象一次性返回
            @Override
            public void onCompleted() {
                StudentResponse studentResponse = StudentResponse.newBuilder().setName("张三").setAge(20).setCity("西安").build();
                StudentResponse studentResponse2 = StudentResponse.newBuilder().setName("李四").setAge(30).setCity("广州").build();
                StudentResponseList studentResponseList = StudentResponseList.newBuilder().addStudentResponse(studentResponse).addStudentResponse(studentResponse2).build();
                responseObserver.onNext(studentResponseList);
                responseObserver.onCompleted();
            }
        };
    }

    /**
     * 客户端与服务端均传送流式参数
     */
    @Override
    public StreamObserver<StreamRequest> biTalk(StreamObserver<StreamResponse> responseObserver) {
        return new StreamObserver<StreamRequest>() {
            // 这里就不像客户端流服务端普通一样需要等待onnext接受完全部请求后，才能包装一个response对象返回，
            // 由于这里是双向流，可以发送一个就返回一个（都是基于一个流管道），
            // 因此直接在onnext中接收一个request就处理形成response，然后调用responseobserver对象的onnext方法构建response对象返回即可。
            @Override
            public void onNext(StreamRequest value) {
                System.out.println(value.getRequestInfo());
                responseObserver.onNext(StreamResponse.newBuilder().setResponseInfo(UUID.randomUUID().toString()).build());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
