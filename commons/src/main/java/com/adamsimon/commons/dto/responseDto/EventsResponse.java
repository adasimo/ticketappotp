package com.adamsimon.commons.dto.responseDto;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.helperDto.EventDetails;

import java.util.List;
import java.util.Objects;

public class EventsResponse extends AbstractPartnerResponse {
    private List<EventDetails> data;
//    Boolean success;

    public EventsResponse() {}

    public EventsResponse(List<EventDetails> data, Boolean success) {
        super(success);
        this.data = data;
    }

    public List<EventDetails> getData() {
        return data;
    }

    public void setData(List<EventDetails> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "EventsResponse{" +
                "data=" + data +
                ", success=" + getSuccess() +
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
