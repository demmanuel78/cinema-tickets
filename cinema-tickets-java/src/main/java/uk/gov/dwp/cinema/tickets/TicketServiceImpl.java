package uk.gov.dwp.cinema.tickets;

import uk.gov.dwp.cinema.tickets.validation.PurchaseValidator;
import uk.gov.dwp.cinema.tickets.validation.ValidationException;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;

import uk.gov.dwp.uc.pairtest.TicketService;   
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

public final class TicketServiceImpl implements TicketService {

    private final TicketPaymentService paymentService;
    private final SeatReservationService seatReservationService;
    private final PurchaseValidator validator = new PurchaseValidator();

    public TicketServiceImpl(TicketPaymentService paymentService,
                             SeatReservationService seatReservationService) {
        this.paymentService = paymentService;
        this.seatReservationService = seatReservationService;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) {
        if (accountId == null) throw new ValidationException("Account id must not be null");

        validator.validate(accountId, ticketTypeRequests);

        int totalToPayInPence = TicketsCalculator.calculateTotalPaymentPence(ticketTypeRequests);
        int seatsToReserve    = TicketsCalculator.calculateSeatsToReserve(ticketTypeRequests);

        if (totalToPayInPence > 0) paymentService.makePayment(accountId, totalToPayInPence);
        if (seatsToReserve > 0)    seatReservationService.reserveSeat(accountId, seatsToReserve);
    }
}
