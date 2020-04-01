package com.adamsimon.commons.dto.responseDto;


import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.helperDto.EventData;

import java.util.Objects;

public class EventDataResponse extends AbstractPartnerResponse {
    EventData data;
//    Boolean success;

    public EventDataResponse() {}

    public EventDataResponse(EventData data, Boolean success) {
        super(success);
        this.data = data;
    }

    public EventData getData() {
        return data;
    }

    public void setData(EventData data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventDataResponse that = (EventDataResponse) o;
        return Objects.equals(data, that.data) &&
                Objects.equals(this.getSuccess(), that.getSuccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, this.getSuccess());
    }

    @Override
    public String toString() {
        return "EventDataResponse{" +
                "data=" + data +
                ", success=" + this.getSuccess() +
                '}';
    }
}
