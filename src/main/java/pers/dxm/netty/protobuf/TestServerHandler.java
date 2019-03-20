package pers.dxm.netty.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by douxm on 2019\3\20 0020.
 */
public class TestServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {
    /**
     * 泛型中传入的是对具体结构体对应的类
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MyDataInfo.MyMessage myMessage) throws Exception {
        MyDataInfo.MyMessage.DataType dataType = myMessage.getDataType();
        // 直接打印对象的属性值即可，因为Initializer中的各种handler已经对客户端传过来的二进制数组进行了解码，运行到自定义handler时，就已经被转换为对象了
        // 下面代码中同样适用打印来代替了具体的业务逻辑
        if (dataType == MyDataInfo.MyMessage.DataType.PersonType) {
            MyDataInfo.Person person = myMessage.getPerson();
            System.out.println(person.getName());
            System.out.println(person.getAge());
            System.out.println(person.getAddress());
        } else if (dataType == MyDataInfo.MyMessage.DataType.DogType) {
            MyDataInfo.Dog dog = myMessage.getDog();
            System.out.println(dog.getName());
            System.out.println(dog.getAge());
        } else {
            MyDataInfo.Cat cat = myMessage.getCat();
            System.out.println(cat.getName());
            System.out.println(cat.getCity());
        }
    }
}
