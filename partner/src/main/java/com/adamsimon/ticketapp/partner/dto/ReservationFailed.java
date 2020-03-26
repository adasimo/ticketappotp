package com.adamsimon.ticketapp.partner.dto;

import com.adamsimon.ticketapp.partner.partnerabstractions.abstracts.AbstractPartnerResponse;

public class ReservationFailed extends AbstractPartnerResponse {
    Integer errorCode;

    public ReservationFailed () {
        super();
    }

    public ReservationFailed(Boolean success, Integer errorCode) {
        this.setSuccess(success);
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
