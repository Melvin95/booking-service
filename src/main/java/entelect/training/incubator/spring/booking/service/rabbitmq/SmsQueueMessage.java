package entelect.training.incubator.spring.booking.service.rabbitmq;

import lombok.Data;

/**
 * {
 *   "phoneNumber": "+27801234567",
 *   "message": "Molo Air: Confirming flight ABS123 booked for Jane Doe on 05-06-2021."
 * }
 */
@Data
public class SmsQueueMessage {

    String phoneNumber;
    String message;

}
