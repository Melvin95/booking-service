package entelect.training.incubator.spring.booking.service.service;

import com.google.gson.Gson;
import entelect.training.incubator.spring.booking.service.config.LoyaltyClient;
import entelect.training.incubator.spring.booking.service.loyalty.CaptureRewardsRequest;
import entelect.training.incubator.spring.booking.service.loyalty.ObjectFactory;
import entelect.training.incubator.spring.booking.service.model.Booking;
import entelect.training.incubator.spring.booking.service.model.BookingSearchRequest;
import entelect.training.incubator.spring.booking.service.model.customers.Customer;
import entelect.training.incubator.spring.booking.service.model.flights.Flight;
import entelect.training.incubator.spring.booking.service.rabbitmq.RabbitMQProducer;
import entelect.training.incubator.spring.booking.service.rabbitmq.SmsQueueMessage;
import entelect.training.incubator.spring.booking.service.repository.BookingRepository;
import net.bytebuddy.dynamic.DynamicType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RestClient.Builder restClient;
    private final LoyaltyClient loyaltyClient;
    private final RabbitMQProducer rabbitMQProducer;

    //Customer service configs
    private final String customerServiceBaseURL = "http://localhost:8201";
    private final String customerServiceUsername = "user";
    private final String customerServicePassword = "the_cake";

    //Flight service configs
    private final String flightServiceBaseURL = "http://localhost:8202";
    private final String flightServiceUsername = "user";
    private final String flightServicePassword = "the_cake";

    public BookingService(BookingRepository bookingRepository, RestClient.Builder restClient, LoyaltyClient loyaltyClient, RabbitMQProducer rabbitMQProducer) {
        this.bookingRepository = bookingRepository;
        this.restClient = restClient;
        this.loyaltyClient = loyaltyClient;
        this.rabbitMQProducer = rabbitMQProducer;
    }

    public Booking createBooking(Booking booking) throws Exception {

        ResponseEntity<Customer> customerResponseEntity = this.restClient.baseUrl(customerServiceBaseURL).build().get()
                .uri("/customers/"+booking.getCustomerId())
                .header("Authorization", getBasicAuthHeader(customerServiceUsername, customerServicePassword))
                .retrieve().toEntity(Customer.class);

        if(!customerResponseEntity.getStatusCode().is2xxSuccessful()){
            //customer doesn't exist
            throw new Exception("Customer doesn't exist!!");
        }

        ResponseEntity<Flight> flightResponseEntity = this.restClient.baseUrl(flightServiceBaseURL).build().get()
                .uri("/flights/"+booking.getFlightId())
                .header("Authorization", getBasicAuthHeader(flightServiceUsername, flightServicePassword))
                .retrieve().toEntity(Flight.class);

        if(!flightResponseEntity.getStatusCode().is2xxSuccessful()){
            throw new Exception("Flight doesn't exist!!!");
        }

        //Capture rewards request
        if(flightResponseEntity.getBody()!=null && customerResponseEntity.getBody()!=null){
            Flight flight = flightResponseEntity.getBody();
            Customer customer = customerResponseEntity.getBody();

            CaptureRewardsRequest captureRewardsRequest = new ObjectFactory().createCaptureRewardsRequest();
            captureRewardsRequest.setAmount(flight.getSeatCost());
            captureRewardsRequest.setPassportNumber(customer.getPassportNumber());
            loyaltyClient.callWebService("http://localhost:8208/ws", captureRewardsRequest);

            SmsQueueMessage smsQueueMessage = new SmsQueueMessage();
            smsQueueMessage.setMessage(String.format("Molo Air: Confirming flight %s booked for %s on %s.",
                    flight.getFlightNumber(),
                    customer.getFirstName() + " "+ customer.getLastName(),
                    flight.getDepartureTime()));
            smsQueueMessage.setPhoneNumber(customer.getPhoneNumber());

            rabbitMQProducer.sendMessage(new Gson().toJson(smsQueueMessage));
        }

        return bookingRepository.save(booking);
    }

    public List<Booking> getBookings(){
        Iterable<Booking> bookingIterable = bookingRepository.findAll();

        List<Booking> bookingList = new ArrayList<>();
        bookingIterable.forEach(bookingList::add);

        return bookingList;
    }

    public Optional<Booking> getBookingByID(Integer bookingID){
        return bookingRepository.findById(bookingID);
    }

    private String getBasicAuthHeader(String username, String password){
        String auth = username + ":" + password;
        byte[] encodedAuth;
        encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String( encodedAuth );
    }

    public Optional<List<Booking>> searchBookings(BookingSearchRequest bookingSearchRequest) throws Exception {

        if(bookingSearchRequest.getCustomerId()!=null){
             return bookingRepository.findByCustomerId(bookingSearchRequest.getCustomerId());
        } else if(bookingSearchRequest.getReferenceNumber()!=null){
            Optional<Booking> booking = this.bookingRepository.findById(bookingSearchRequest.getReferenceNumber());
            if(booking.isPresent()){
                List<Booking> bookingList = new ArrayList<>();
                bookingList.add(booking.get());
                return Optional.of(bookingList);
            }
        }
        throw new Exception("Booking doesn't exist!!!");
    }

}
