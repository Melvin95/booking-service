package entelect.training.incubator.spring.booking.service;

import entelect.training.incubator.spring.booking.service.config.LoyaltyClient;
import entelect.training.incubator.spring.booking.service.config.LoyaltyServiceConfig;
import entelect.training.incubator.spring.booking.service.loyalty.CaptureRewardsRequest;
import entelect.training.incubator.spring.booking.service.loyalty.CaptureRewardsResponse;
import entelect.training.incubator.spring.booking.service.loyalty.ObjectFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LoyaltyServiceConfig.class, loader = AnnotationConfigContextLoader.class)
public class LoyaltyServiceClientTest {

    @Autowired
    LoyaltyClient loyaltyClient;

    @Test
    public void testRewardBalanceCapture(){
        CaptureRewardsRequest captureRewardsRequest = new ObjectFactory().createCaptureRewardsRequest();
        captureRewardsRequest.setAmount(new BigDecimal(0));
        captureRewardsRequest.setPassportNumber("4568486486");
        CaptureRewardsResponse captureRewardsResponse = (CaptureRewardsResponse)loyaltyClient.callWebService("http://localhost:8208/ws",captureRewardsRequest);
        assertEquals(new BigDecimal("895.0"), captureRewardsResponse.getBalance());
    }
}
