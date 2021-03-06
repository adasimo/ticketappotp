package com.adamsimon.partner.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "TicketModuleUser")
public class TicketModuleUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;
    private String token;

    public TicketModuleUser() {}

    public TicketModuleUser(String token) {
        this.token = token;
    }
    public TicketModuleUser(Long id, String token) {
        this.id = id;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "TicketModuleUser{" +
                "id=" + id +
                ", token='" + token + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketModuleUser that = (TicketModuleUser) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getToken(), that.getToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getToken());
    }
}
