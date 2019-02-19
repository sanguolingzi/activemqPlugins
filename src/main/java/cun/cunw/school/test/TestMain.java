package cun.cunw.school.test;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;

public class TestMain {


    public static void main(String[] args) throws Exception{
        receive();
        //send();
    }

    public static void  receive() throws Exception{
        Session session = null;
        Connection connection = null;
        try{
            ActiveMQConnectionFactory connectionFactory
                    = new ActiveMQConnectionFactory("admin", "admin", "tcp://localhost:61616");
            connection =  connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false,2);
            Destination destination = session.createQueue("test1111");
            Message message = session.createConsumer(destination).receive();
            System.out.println("message:"+message);
            message.acknowledge();
            /*
            session.setMessageListener(new MessageListener() {
                public void onMessage(Message message) {
                    System.out.println("message:"+message);
                }
            });
            */
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(session != null)
                session.close();
            if(connection != null)
                connection.close();
        }
    }

    public static void  send() throws Exception{
        Session session = null;
        ActiveMQConnection activeMQConnection = null;
        try{
            ActiveMQConnectionFactory connectionFactory
                    = new ActiveMQConnectionFactory("admin", "admin", "tcp://localhost:61616");
            activeMQConnection =  (ActiveMQConnection)connectionFactory.createConnection();
            System.out.print("activeMQConnection:"+activeMQConnection);
            session = activeMQConnection.createQueueSession(false,2);
            Queue queue =session.createQueue("test1111");

            MessageProducer producer = session.createProducer(queue);
            //TextMessage textMessage = new ActiveMQTextMessage();
            //textMessage.setText("123456");

            ActiveMQMapMessage message = new ActiveMQMapMessage();
            message.setString("aaaaaa","bbbbbbb");
            message.setInt("ccc",123);
            producer.send(message);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(session != null)
                session.close();
            if(activeMQConnection != null)
                activeMQConnection.close();
        }
    }
}
