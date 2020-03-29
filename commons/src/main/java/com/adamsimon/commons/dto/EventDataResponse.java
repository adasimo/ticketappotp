package com.adamsimon.commons.dto;


import java.util.Objects;

public class EventDataResponse {
    EventData data;
    Boolean success;

    public EventDataResponse() {}

    public EventDataResponse(EventData data, Boolean success) {
        this.data = data;
        this.success = success;
    }

    public EventData getData() {
        return data;
    }

    public void setData(EventData data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventDataResponse that = (EventDataResponse) o;
        return Objects.equals(data, that.data) &&
                Objects.equals(success, that.success);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, success);
    }

    @Override
    public String toString() {
        return "EventDataResponse{" +
                "data=" + data +
                ", success=" + success +
                '}';
    }
}
