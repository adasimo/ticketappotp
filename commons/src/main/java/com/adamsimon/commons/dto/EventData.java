package com.adamsimon.commons.dto;

import java.util.Arrays;
import java.util.Objects;

public class EventData {
    Long eventId;
    Seat[] seats;

    public EventData() {}

    public EventData(Long eventId, Seat[] seats) {
        this.eventId = eventId;
        this.seats = seats;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Seat[] getSeats() {
        return seats;
    }

    public void setSeats(Seat[] seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return "EventData{" +
                "eventId=" + eventId +
                ", seats=" + Arrays.toString(seats) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventData eventData = (EventData) o;
        return Objects.equals(getEventId(), eventData.getEventId()) &&
                Arrays.equals(getSeats(), eventData.getSeats());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getEventId());
        result = 31 * result + Arrays.hashCode(getSeats());
        return result;
    }
}
