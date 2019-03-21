package pers.dxm.thrift.demo;

import org.apache.thrift.TException;
import pers.dxm.thrift.source.DataException;
import pers.dxm.thrift.source.Person;
import pers.dxm.thrift.source.PersonService;

/**
 * Created by douxm on 2019\3\21 0021.
 */
public class PersonServiceImpl implements PersonService.Iface {
    @Override
    public Person getPersonByUsername(String username) throws DataException, TException {
        // 接收到客户端的消息并打印
        System.out.println("Got Client Param: " + username);
        // 经过服务端的业务处理后将对象返回给客户端
        Person person = new Person();
        person.setUsername(username);
        person.setAge(20);
        person.setMarried(false);
        return person;
    }

    @Override
    public void savePerson(Person person) throws DataException, TException {
        System.out.println("Got Client Param: ");
        // 打印模拟服务端的save逻辑
        System.out.println(person.getUsername());
        System.out.println(person.getAge());
        System.out.println(person.isMarried());
    }
}
