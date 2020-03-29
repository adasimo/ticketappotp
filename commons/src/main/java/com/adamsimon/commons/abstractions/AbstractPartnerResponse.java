package com.adamsimon.commons.abstractions;

public abstract class AbstractPartnerResponse {
    Boolean success;

    public AbstractPartnerResponse() {}

    public AbstractPartnerResponse(Boolean success) {
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
