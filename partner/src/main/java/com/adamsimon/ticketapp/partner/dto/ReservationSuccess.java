package com.adamsimon.ticketapp.partner.dto;

import com.adamsimon.ticketapp.partner.partnerabstractions.abstracts.AbstractPartnerResponse;

public class ReservationSuccess extends AbstractPartnerResponse {
    Long reservationId;

    public ReservationSuccess() { super(); }

    public ReservationSuccess(Boolean success, Long reservationId) {
        this.setSuccess(success);
        this.reservationId = reservationId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
}
