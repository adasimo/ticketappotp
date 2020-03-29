package com.adamsimon.commons.enums;

public enum FilesEnum {
    GETEVENT1 ("getEvent1.json"),
    GETEVENT2 ("getEvent2.json"),
    GETEVENT3 ("getEvent3.json"),
    GETEVENTS ("getEvents.json"),
    RESERVE ("reserve.json");

    private final String name;

    private FilesEnum(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
