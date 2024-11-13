package entelect.training.incubator.spring.booking.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class LoyaltyServiceConfig {

    @Bean
    public Jaxb2Marshaller marshaller(){
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath("entelect.training.incubator.spring.booking.service.loyalty");
        return jaxb2Marshaller;
    }

    @Bean
    public LoyaltyClient getLoyaltyClient(Jaxb2Marshaller marshaller){
        LoyaltyClient loyaltyClient = new LoyaltyClient();
        loyaltyClient.setDefaultUri("http://localhost:8208/ws");
        loyaltyClient.setMarshaller(marshaller);
        loyaltyClient.setUnmarshaller(marshaller);
        return loyaltyClient;
    }
}
