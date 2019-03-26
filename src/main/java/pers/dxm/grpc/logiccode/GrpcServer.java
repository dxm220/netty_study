package pers.dxm.grpc.logiccode;

import io.grpc.Server;
import io.grpc.ServerBuilder;

/**
 * Created by douxm on 2019\3\25 0025.
 */
public class GrpcServer {
    private Server server;

    private void start() throws Exception {
        this.server = ServerBuilder.forPort(8899).addService(new StudentServiceImpl()).build().start();
        System.out.println("server started!");
        // 服务器关闭之前，会调用该方法（回调钩子），可以将一些关闭之前需要做的事情放到这个方法中
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("关闭jvm");
            GrpcServer.this.stop();
        }));
    }

    /**
     * 关闭服务器的方法
     */
    private void stop() {
        if (null != this.server) {
            this.server.shutdown();
        }
    }

    /**
     * 该方法可以使得服务器启动后一直处于开启等待状态，可以理解为服务器启动后的死循环，不显式的调用关闭方法服务器不会停止
     */
    private void awaitTermination() throws Exception {
        if (null != this.server) {
            this.server.awaitTermination();
        }
    }

    public static void main(String[] args) throws Exception {
        GrpcServer server = new GrpcServer();
        server.start();
        server.awaitTermination();
    }
}
