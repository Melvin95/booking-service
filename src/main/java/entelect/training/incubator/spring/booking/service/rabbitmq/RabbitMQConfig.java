package entelect.training.incubator.spring.booking.service.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

   @Bean public Exchange exchange(){
       return new DirectExchange("notification-exchange", false, false);
   }
}
