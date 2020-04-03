package com.adamsimon.commons.dto.helperDto;

import com.adamsimon.commons.enums.CurrencyEnum;

import java.util.Objects;

public class Seat {
    private String id;
    private Integer price;
    private CurrencyEnum currency;
    private Boolean reserved;

    public Seat() {}

    public Seat(String id, Integer price, CurrencyEnum currency, Boolean reserved) {
        this.id = id;
        this.price = price;
        this.currency = currency;
        this.reserved = reserved;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public CurrencyEnum getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyEnum currency) {
        this.currency = currency;
    }

    public Boolean getReserved() {
        return reserved;
    }

    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id='" + id + '\'' +
                ", price=" + price +
                ", currency=" + currency +
                ", reserved=" + reserved +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return Objects.equals(getId(), seat.getId()) &&
                Objects.equals(getPrice(), seat.getPrice()) &&
                getCurrency() == seat.getCurrency() &&
                Objects.equals(getReserved(), seat.getReserved());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPrice(), getCurrency(), getReserved());
    }
}
