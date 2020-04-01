package com.adamsimon.commons.dto.responseDto;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;

public class ReservationFailedResponse extends AbstractPartnerResponse {

    Integer errorCode;
    String errorMessage;

    public ReservationFailedResponse() {
        super();
    }

    public ReservationFailedResponse(Boolean success, Integer errorCode, String errorMessage) {
        this.setSuccess(success);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "{ \n" +
                "\"success\": " + "\"" + getSuccess() + "\", \n" +
                "\"text\": " + "\"" + errorMessage + "\", \n" +
                "\"code\": " + "\"" + errorCode + "\"\n" +
                "}";
    }
}
