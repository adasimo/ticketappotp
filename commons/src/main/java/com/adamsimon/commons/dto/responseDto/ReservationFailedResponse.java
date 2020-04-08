package com.adamsimon.commons.dto.responseDto;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;

import java.util.Objects;

public class ReservationFailedResponse extends AbstractPartnerResponse {

    private Integer errorCode;
    private String errorMessage;

    public ReservationFailedResponse() {
        super();
    }

    public ReservationFailedResponse(Boolean success, Integer errorCode, String errorMessage) {
        super(success);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationFailedResponse that = (ReservationFailedResponse) o;
        return Objects.equals(getErrorCode(), that.getErrorCode()) &&
                Objects.equals(getErrorMessage(), that.getErrorMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getErrorCode(), getErrorMessage());
    }
}
