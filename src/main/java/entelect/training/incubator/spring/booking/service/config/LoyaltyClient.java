package entelect.training.incubator.spring.booking.service.config;

import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.math.BigDecimal;

public class LoyaltyClient extends WebServiceGatewaySupport {

    public Object callWebService(String url, Object request){
        return getWebServiceTemplate().marshalSendAndReceive(url, request);
    }
}
