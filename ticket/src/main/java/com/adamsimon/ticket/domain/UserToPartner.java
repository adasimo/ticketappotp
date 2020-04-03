package com.adamsimon.ticket.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "UserToPartner")
public class UserToPartner {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;
    private String token;

    public UserToPartner() {}

    public UserToPartner(String token) {
        this.token = token;
    }
    public UserToPartner(Long id, String token) {
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
        UserToPartner that = (UserToPartner) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getToken(), that.getToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getToken());
    }
}
