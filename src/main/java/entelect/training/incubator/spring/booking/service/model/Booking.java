package entelect.training.incubator.spring.booking.service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Booking {


    private Integer customerId;
    private Integer flightId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer referenceNumber;
}
