package com.adamsimon.commons.dto;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EventsResponse {
    List<EventDetails> data;
    Boolean success;

    public EventsResponse() {}

    public EventsResponse(List<EventDetails> data, Boolean success) {
        this.data = data;
        this.success = success;
    }

    public List<EventDetails> getData() {
        return data;
    }

    public void setData(List<EventDetails> data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "EventsResponse{" +
                "data=" + data +
                ", success=" + success +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventsResponse that = (EventsResponse) o;
        return Objects.equals(getData(), that.getData()) &&
                Objects.equals(getSuccess(), that.getSuccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getData(), getSuccess());
    }
}
