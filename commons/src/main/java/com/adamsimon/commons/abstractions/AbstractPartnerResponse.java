package com.adamsimon.commons.abstractions;

public abstract class AbstractPartnerResponse {
    private Boolean success;

    public AbstractPartnerResponse() { super(); }

    public AbstractPartnerResponse(Boolean success) {
        super();
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
