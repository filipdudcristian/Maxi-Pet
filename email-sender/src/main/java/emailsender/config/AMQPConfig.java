package emailsender.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "repository")
public class AMQPConfig {

    @Bean
    public Queue emailQueue() {
        return new Queue("email-queue", false);
    }

    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange("email-exchange");
    }

    @Bean
    public Binding binding(Queue emailQueue, DirectExchange emailExchange) {
        return BindingBuilder.bind(emailQueue).to(emailExchange).with("email-routing-key");
    }
}

