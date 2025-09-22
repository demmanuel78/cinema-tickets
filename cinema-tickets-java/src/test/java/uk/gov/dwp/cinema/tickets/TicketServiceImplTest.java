
package uk.gov.dwp.cinema.tickets;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;                
import static uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type.*; 

import uk.gov.dwp.cinema.tickets.validation.ValidationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketServiceImplTest {

    @Test
    void happy_path_calculates_and_calls_services() {
        var payment = Mockito.mock(TicketPaymentService.class);
        var seats   = Mockito.mock(SeatReservationService.class);

        var service = new TicketServiceImpl(payment, seats);

        long accountId = 123L;
        var reqs = new TicketTypeRequest[] {
            new TicketTypeRequest(ADULT, 2),  // £50.00
            new TicketTypeRequest(CHILD, 1),  // £15.00
            new TicketTypeRequest(INFANT, 1)  // £0
        };

        service.purchaseTickets(accountId, reqs);

        verify(payment).makePayment(eq(accountId), eq(6500));
        verify(seats).reserveSeat(eq(accountId), eq(3));
    }

    @Test
    void rejects_account_id_zero_or_negative() {
        var service = new TicketServiceImpl(mock(TicketPaymentService.class), mock(SeatReservationService.class));
        assertThrows(ValidationException.class,
            () -> service.purchaseTickets(0L, new TicketTypeRequest(ADULT, 1)));
        assertThrows(ValidationException.class,
            () -> service.purchaseTickets(-1L, new TicketTypeRequest(ADULT, 1)));
    }

    @Test
    void rejects_no_tickets_or_over_25() {
        var service = new TicketServiceImpl(mock(TicketPaymentService.class), mock(SeatReservationService.class));
        assertThrows(ValidationException.class, () -> service.purchaseTickets(1L));
        assertThrows(ValidationException.class, () ->
            service.purchaseTickets(1L, new TicketTypeRequest(ADULT, 26)));
    }

    @Test
    void rejects_children_or_infants_without_adult() {
        var service = new TicketServiceImpl(mock(TicketPaymentService.class), mock(SeatReservationService.class));
        assertThrows(ValidationException.class,
            () -> service.purchaseTickets(1L, new TicketTypeRequest(CHILD, 1)));
        assertThrows(ValidationException.class,
            () -> service.purchaseTickets(1L, new TicketTypeRequest(INFANT, 1)));
    }

    @Test
    void rejects_more_infants_than_adults() {
        var service = new TicketServiceImpl(mock(TicketPaymentService.class), mock(SeatReservationService.class));
        assertThrows(ValidationException.class,
            () -> service.purchaseTickets(1L,
                  new TicketTypeRequest(ADULT, 1),
                  new TicketTypeRequest(INFANT, 2)));
    }
}
