package ru.logisticplatform.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProducerRabbitMq {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ProducerRabbitMq(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${rabbitmq.exchange}")
    private String directExchange;

    @Value("${unknown.user.key}")
    private String unknownUserKey;

    @Value("${unknown.user.queue}")
    private String unknownUserQueue;

    @Bean
    public Queue unknownUserQueue(){
        return new Queue(unknownUserQueue, false);
    }

    @Bean
    public TopicExchange directExchange(){
        return new TopicExchange(directExchange);
    }

    @Bean
    public Binding unknownUserBinding(){
        return BindingBuilder.bind(unknownUserQueue()).to(directExchange()).with(unknownUserKey);
    }

    public void unknownUserProduce(String userName){
        log.info("IN ProducerRabbitMq unknownUserProduce - unknown user: {}", userName);

        rabbitTemplate.convertAndSend(directExchange, unknownUserKey, userName);
    }
}
