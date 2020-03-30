package com.adamsimon.commons.dto;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;

public class ReservationSuccessResponse extends AbstractPartnerResponse {
    Long reservationId;

    public ReservationSuccessResponse() { super(); }

    public ReservationSuccessResponse(Boolean success, Long reservationId) {
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
