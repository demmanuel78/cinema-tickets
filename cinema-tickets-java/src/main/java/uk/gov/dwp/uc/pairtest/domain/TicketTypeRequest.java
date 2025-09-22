package uk.gov.dwp.uc.pairtest.domain;

/**
 * Immutable Object
 */

import java.util.Objects;

public record TicketTypeRequest(Type type, int noOfTickets) {

    public enum Type { INFANT, CHILD, ADULT }

    public TicketTypeRequest {
        Objects.requireNonNull(type, "type must not be null");
        if (noOfTickets < 0) {
            throw new IllegalArgumentException("noOfTickets must be >= 0");
        }
    }
}

