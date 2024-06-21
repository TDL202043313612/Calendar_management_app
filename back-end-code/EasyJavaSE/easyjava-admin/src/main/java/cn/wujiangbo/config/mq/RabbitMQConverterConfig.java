//package cn.wujiangbo.config.mq;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.AcknowledgeMode;
//import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
//import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.converter.MappingJackson2MessageConverter;
//import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
//
///**
// * 做序列化，设置回调函数，处理消息失败的场景
// *
// */
//@Configuration
//@Slf4j
//public class RabbitMQConverterConfig implements RabbitListenerConfigurer {
//
//    @Autowired
//    public ConnectionFactory connectionFactory;
//
//    int count = 5;//最大重试次数
//
//    @Bean
//    public DefaultMessageHandlerMethodFactory myHandlerMethodFactory() {
//        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
//        // 这里的转换器设置实现了 通过 @Payload 注解 自动反序列化message body
//        factory.setMessageConverter(new MappingJackson2MessageConverter());
//        return factory;
//    }
//
//    @Override
//    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
//        registrar.setMessageHandlerMethodFactory(myHandlerMethodFactory());
//    }
//
//    @Bean
//    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setConcurrentConsumers(3);
//        factory.setMaxConcurrentConsumers(10);
//        //设置手动签收
//        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//        return factory;
//    }
//
//    @Bean
//    public RabbitTemplate rabbitTemplate() {
//        RabbitTemplate template = new RabbitTemplate(connectionFactory);
//        // 设置开启Mandatory,才能触发回调函数,⽆论消息推送结果怎么样都强制调⽤回调函数
//        template.setMandatory(true);
//        // 这里的转换器设置，实现了发送消息时自动序列化消息对象为message body
//        template.setMessageConverter(new Jackson2JsonMessageConverter());
//
//        /**
//         * 不管消息发送到交换机是否成功，该方法都会被回调
//         * @param correlationData：相关数据，发送的时候可以指定一个correlationData，回调的时候传回来给我
//         * @param ack：true表示消息发到交换机成功，false表示消息发送交换机失败
//         * @param cause：失败原因
//         */
//        template.setConfirmCallback((correlationData, ack, cause) ->{
//            if(ack){
//                log.info("消息发送到交换机成功...");
//            }else{
//                log.info("消息发送到交换机失败...");
//            }
//        });
//
//        /**
//         * 消息发到队列失败时才会回调该方法
//         * @param message：封装消息内容的对象
//         * @param replyCode：错误码
//         * @param replyText：错误对象
//         * @param exchange：交换机
//         * @param routingKey：路由键
//         */
//        template.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
//            log.info("消息从交换机路由到队列失败，message={}，replyText={}，exchange={}，routingKey={}", message, replyText, exchange, routingKey);
//            if(count > 0){
//                log.info("开始重试....");
//                //重试发送消息
//                template.convertAndSend(exchange, routingKey, message);
//                count--;
//            }
//        });
//        return template;
//    }
//}