package com.adamsimon.commons.dto.responseDto;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;

import java.util.Objects;

public class ReservationSuccessResponse extends AbstractPartnerResponse {
    private Long reservationId;

    public ReservationSuccessResponse() { super(); }

    public ReservationSuccessResponse(Boolean success, Long reservationId) {
        super(success);
        this.reservationId = reservationId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    @Override
    public String toString() {
        return "ReservationSuccessResponse{" +
                "success=" + getSuccess() +
                "reservationId=" + reservationId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationSuccessResponse that = (ReservationSuccessResponse) o;
        return Objects.equals(getReservationId(), that.getReservationId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReservationId());
    }
}
