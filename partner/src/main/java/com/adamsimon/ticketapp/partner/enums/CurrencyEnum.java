package com.adamsimon.ticketapp.partner.enums;

public enum CurrencyEnum {
    HUF ("HUF"),
    EUR ("EUR"),
    USD ("USD");

    private final String name;

    private CurrencyEnum(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
