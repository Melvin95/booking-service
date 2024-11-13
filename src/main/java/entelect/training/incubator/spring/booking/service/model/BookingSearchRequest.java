package entelect.training.incubator.spring.booking.service.model;

import lombok.Data;

@Data
public class BookingSearchRequest {
    private Integer customerId;
    private Integer referenceNumber;
}
