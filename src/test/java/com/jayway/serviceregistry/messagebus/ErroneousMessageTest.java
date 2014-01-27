package com.jayway.serviceregistry.messagebus;

import com.jayway.serviceregistry.boot.ServiceRegistryStart;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.jayway.awaitility.Awaitility.await;
import static com.jayway.awaitility.Awaitility.to;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceRegistryStart.class)
public class ErroneousMessageTest {

    @Autowired
    AmqpTemplate amqpTemplate;

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    TopicExchange lab;


    @Ignore @Test public void
    logs_are_sent_when_message_is_erroneous()  {
        // Given
        Queue logQueue = amqpAdmin.declareQueue();
        Binding binding = BindingBuilder.bind(logQueue).to(lab).with(Topic.LOG.getRoutingKey());

        try {
            // When
            amqpTemplate.convertAndSend(Topic.getLabExchange(), "service", "error");

            // Then
            Message message = await().atMost(1, SECONDS).untilCall(to(amqpTemplate).receive(logQueue.getName()), notNullValue());
            System.out.println(message);
        } finally {
            amqpAdmin.deleteQueue(logQueue.getName());
            amqpAdmin.removeBinding(binding);
        }
    }
}
