package com.adamsimon.commons.dto.helperDto;

import java.util.Objects;

public class EventDetails {
    private Long eventId;
    private String title;
    private String location;
    private String startTimeStamp;
    private String endTimeStamp;

    public EventDetails() {}

    public EventDetails(Long eventId, String title, String location, String startTimeStamp, String endTimeStamp) {
        this.eventId = eventId;
        this.title = title;
        this.location = location;
        this.startTimeStamp = startTimeStamp;
        this.endTimeStamp = endTimeStamp;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(String startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public String getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(String endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    @Override
    public String toString() {
        return "EventDetails{" +
                "eventId=" + eventId +
                ", title='" + title + '\'' +
                ", Location='" + location + '\'' +
                ", startTimeStamp=" + startTimeStamp +
                ", endTimeStamp=" + endTimeStamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventDetails that = (EventDetails) o;
        return Objects.equals(eventId, that.eventId) &&
                Objects.equals(title, that.title) &&
                Objects.equals(location, that.location) &&
                Objects.equals(startTimeStamp, that.startTimeStamp) &&
                Objects.equals(endTimeStamp, that.endTimeStamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, title, location, startTimeStamp, endTimeStamp);
    }
}
