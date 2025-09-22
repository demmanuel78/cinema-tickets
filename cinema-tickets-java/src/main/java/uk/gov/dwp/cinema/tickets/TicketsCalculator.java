package uk.gov.dwp.cinema.tickets;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;  

public final class TicketsCalculator {

    static final int ADULT_PRICE_P = 2500;
    static final int CHILD_PRICE_P = 1500;

    public static int calculateTotalPaymentPence(TicketTypeRequest... requests) {
        int total = 0;
        if (requests == null) return 0;
        for (TicketTypeRequest r : requests) {
            if (r == null) continue;
            total += switch (r.type()) {
                case ADULT -> ADULT_PRICE_P * r.noOfTickets();
                case CHILD -> CHILD_PRICE_P * r.noOfTickets();
                case INFANT -> 0;
            };
        }
        return total;
    }

    public static int calculateSeatsToReserve(TicketTypeRequest... requests) {
        int seats = 0;
        if (requests == null) return 0;
        for (TicketTypeRequest r : requests) {
            if (r == null) continue;
            seats += switch (r.type()) {
                case ADULT, CHILD -> r.noOfTickets();
                case INFANT -> 0;
            };
        }
        return seats;
    }
}
