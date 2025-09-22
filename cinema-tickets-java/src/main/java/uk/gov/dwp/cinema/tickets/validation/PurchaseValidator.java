package uk.gov.dwp.cinema.tickets.validation;

import static uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type.*; 
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;              

public final class PurchaseValidator {

    public void validate(long accountId, TicketTypeRequest... requests) {
        if (accountId <= 0) throw new ValidationException("Account id must be > 0");
        if (requests == null || requests.length == 0) {
            throw new ValidationException("At least one ticket type must be requested");
        }

        int infants = 0, children = 0, adults = 0, total = 0;

        for (TicketTypeRequest r : requests) {
            if (r == null) continue;
            total += r.noOfTickets();
            switch (r.type()) {
                case INFANT -> infants += r.noOfTickets();
                case CHILD  -> children += r.noOfTickets();
                case ADULT  -> adults  += r.noOfTickets();
            }
        }

        if (total == 0) throw new ValidationException("Total tickets must be > 0");
        if (total > 25) throw new ValidationException("Cannot purchase more than 25 tickets");

        boolean hasChildrenOrInfants = (children + infants) > 0;
        if (hasChildrenOrInfants && adults == 0) {
            throw new ValidationException("Child or Infant tickets require at least one Adult");
        }

        if (infants > adults) {
            throw new ValidationException("Infants cannot exceed Adults (one lap per adult)");
        }
    }
}
