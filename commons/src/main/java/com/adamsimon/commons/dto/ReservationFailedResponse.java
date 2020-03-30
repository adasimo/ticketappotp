package com.adamsimon.commons.dto;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;

public class ReservationFailedResponse extends AbstractPartnerResponse {
    Integer errorCode;

    public ReservationFailedResponse() {
        super();
    }

    public ReservationFailedResponse(Boolean success, Integer errorCode) {
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
