package pers.dxm.netty.protobuf;

/**
 * Created by douxm on 2019\3\20 0020.
 */
public class TestProtobuf {
    public static void main(String[] args) throws Exception {
        // 模拟客户端首先通过build的set方法构造出person对象（链式编程的模式）
        MyDataInfo.Person person = MyDataInfo.Person.newBuilder().
                setName("张三").setAge(20).setAddress("北京").build();
        // 将person转换为二进制数组，准备通过网络发送至服务端
        byte[] personByteArray = person.toByteArray();
        // 模拟服务端接收到二进制数组后，将二进制数组转换为person对象，并在服务端打印person的信息
        MyDataInfo.Person person2 = MyDataInfo.Person.parseFrom(personByteArray);
        System.out.println(person2.getName());
        System.out.println(person2.getAge());
        System.out.println(person2.getAddress());
    }
}
