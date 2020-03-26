package com.adamsimon.ticketapp.partner.dto;

import java.util.Arrays;
import java.util.Objects;

public class EventsResponse {
    EventDetails[] data;
    Boolean success;

    public EventsResponse() {}

    public EventsResponse(EventDetails[] data, Boolean success) {
        this.data = data;
        this.success = success;
    }

    public EventDetails[] getData() {
        return data;
    }

    public void setData(EventDetails[] data) {
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
                "data=" + Arrays.toString(data) +
                ", success=" + success +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventsResponse that = (EventsResponse) o;
        return Arrays.equals(getData(), that.getData()) &&
                Objects.equals(getSuccess(), that.getSuccess());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getSuccess());
        result = 31 * result + Arrays.hashCode(getData());
        return result;
    }
}
