package entelect.training.incubator.spring.booking.service.model.customers;

import lombok.Data;

/**
 * {
 *     "id": 1,
 *     "username": "jane@doe.inc",
 *     "firstName": "Jane",
 *     "lastName": "Doe",
 *     "passportNumber": "4568486486",
 *     "email": "jane@doe.inc",
 *     "phoneNumber": "+27 80 123 4567"
 * }
 */
@Data
public class Customer {
    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String passportNumber;
    private String email;
    private String phoneNumber;
}
