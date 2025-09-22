
package uk.gov.dwp.cinema.tickets;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;                
import static uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type.*; 

class TicketsCalculatorTest {

    @Test
    void payment_includes_adult_and_child_only() {
        var reqs = new TicketTypeRequest[] {
            new TicketTypeRequest(ADULT, 2),  // 2 * 2500 = 5000
            new TicketTypeRequest(CHILD, 3),  // 3 * 1500 = 4500
            new TicketTypeRequest(INFANT, 5)  // 0
        };
        assertEquals(9500, TicketsCalculator.calculateTotalPaymentPence(reqs));
    }

    @Test
    void seats_exclude_infants() {
        var reqs = new TicketTypeRequest[] {
            new TicketTypeRequest(ADULT, 1),
            new TicketTypeRequest(CHILD, 2),
            new TicketTypeRequest(INFANT, 10)
        };
        assertEquals(3, TicketsCalculator.calculateSeatsToReserve(reqs));
    }
}

