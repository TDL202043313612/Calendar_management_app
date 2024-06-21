//package cn.wujiangbo.config.mq;
//
//import cn.wujiangbo.constants.Constants;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitAdmin;
//
///**
// * <p>声明交换机、声明队列、以及声明绑定关系</p>
// *
// */
//@Configuration
//public class RabbitMQDeclare {
//
//    //注册交换机
//    @Bean
//    public Exchange topicExchange(){
//        //durable：设置持久化
//        return ExchangeBuilder.topicExchange(Constants.MQ.EXCHANGE_EASYJAVA_TOPIC).durable(true).build();
//    }
//
//    //注册邮件队列-用来发邮件
//    @Bean
//    public Queue emailQueue(){
//        return new Queue(Constants.MQ.QUEUE_EMAIL, true, false, false);
//    }
//
//    //邮件队列绑定到交换机
//    @Bean
//    public Binding bindEmailQueue(){
//        return BindingBuilder.bind(emailQueue()).to(topicExchange()).with(Constants.MQ.ROUTINGKEY_QUEUE_EMAIL).noargs();
//    }
//
//    //初始化RabbitAdmin对象
//    @Bean
//    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
//        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
//        // 只有设置为 true，spring 才会加载 RabbitAdmin 这个类
//        rabbitAdmin.setAutoStartup(true);
//
//        //下面设置目的：项目启动时，就创建交换机和队列
//        //创建交换机
//        rabbitAdmin.declareExchange(topicExchange());
//        //创建对列
//        rabbitAdmin.declareQueue(emailQueue());
//
//        return rabbitAdmin;
//    }
//}