package entelect.training.incubator.spring.booking.service.controller;

import entelect.training.incubator.spring.booking.service.loyalty.CaptureRewardsRequest;
import entelect.training.incubator.spring.booking.service.loyalty.ObjectFactory;
import entelect.training.incubator.spring.booking.service.model.BookingSearchRequest;
import entelect.training.incubator.spring.booking.service.service.BookingService;
import entelect.training.incubator.spring.booking.service.model.Booking;
import entelect.training.incubator.spring.booking.service.config.LoyaltyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("bookings")
public class BookingController {

    private final Logger LOG = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;
    private final LoyaltyClient loyaltyClient;

    public BookingController(BookingService bookingService, LoyaltyClient loyaltyClient) {
        this.bookingService = bookingService;
        this.loyaltyClient = loyaltyClient;
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking){
        try{
            LOG.info("Processing booking request {}", booking);
            return new ResponseEntity<>(bookingService.createBooking(booking), HttpStatus.CREATED);
        } catch (Exception e){
            LOG.error("Unable to handle booking request!!",e);
        }
        return ResponseEntity.internalServerError().build();    }

    @GetMapping
    public ResponseEntity<?> getBookings(){
        try {
            LOG.info("Getting all bookings!");
            return new ResponseEntity<>(bookingService.getBookings(),HttpStatus.OK);
        }catch (Exception e){
            LOG.error("Unable to get bookings!!",e);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("{bookingID}")
    public ResponseEntity<?> getBookingByID(@PathVariable Integer bookingID){
        try {
            Optional<Booking> booking = bookingService.getBookingByID(bookingID);
            if(booking.isPresent()){
                return new ResponseEntity<>(booking.get(),HttpStatus.OK);
            }
        } catch (Exception e){
            LOG.error("Unable to get booking!", e);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchBookings(@RequestBody BookingSearchRequest bookingSearchRequest){
        try {
            Optional<List<Booking>> bookingList = bookingService.searchBookings(bookingSearchRequest);
            if(bookingList.isPresent()){
                return new ResponseEntity<>(bookingList.get(),HttpStatus.OK);
            }
        } catch (Exception e){
            LOG.error("Unable to get booking!!!",e);
        }

        return ResponseEntity.notFound().build();
    }
}
