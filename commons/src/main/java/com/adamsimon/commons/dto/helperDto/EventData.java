package com.adamsimon.commons.dto.helperDto;

import java.util.List;
import java.util.Objects;

public class EventData {
    Long eventId;
    List<Seat> seats;

    public EventData() {}

    public EventData(Long eventId, List<Seat> seats) {
        this.eventId = eventId;
        this.seats = seats;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return "EventData{" +
                "eventId=" + eventId +
                ", seats=" + seats +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventData eventData = (EventData) o;
        return Objects.equals(getEventId(), eventData.getEventId()) &&
                Objects.equals(getSeats(), eventData.getSeats());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEventId(), getSeats());
    }
}
