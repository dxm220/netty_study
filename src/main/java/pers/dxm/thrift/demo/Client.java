package pers.dxm.thrift.demo;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import pers.dxm.thrift.source.Person;
import pers.dxm.thrift.source.PersonService;

/**
 * Created by douxm on 2019\3\21 0021.
 */
public class Client {
    public static void main(String[] args) {
        TTransport transport = new TFramedTransport(new TSocket("localhost", 8899), 600);
        TProtocol protocol = new TCompactProtocol(transport);
        PersonService.Client client = new PersonService.Client(protocol);

        try {
            transport.open();
            Person person = client.getPersonByUsername("张三");
            System.out.println(person.getUsername());
            System.out.println(person.getAge());
            System.out.println(person.isMarried());

            System.out.println("-------");

            Person person2 = new Person();
            person2.setUsername("李四");
            person2.setAge(30);
            person2.setMarried(true);
            client.savePerson(person2);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        } finally {
            transport.close();
        }
    }
}
