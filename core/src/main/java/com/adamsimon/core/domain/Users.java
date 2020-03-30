package com.adamsimon.core.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Users")
//@SecondaryTable(name = "UserToken", pkJoinColumns = @PrimaryKeyJoinColumn(name = "userId", referencedColumnName = "userId"))
public class Users {

    public Users() {}

    public Users(Long userId, String name, String email, List<String> token) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.token = token;
    }

    @Id
    @GenericGenerator(name = "userIdGen", strategy = "com.adamsimon.core.generators.UserIdGenerator")
    @GeneratedValue(generator = "userIdGen")
    @Column(name = "userId", nullable = false)
    Long userId;
    @NotNull(message = "{name.required}")
    @Column(name = "name", length = 100)
    String name;
    @NotNull(message = "{email.required}")
    @Pattern(regexp = "\\w+@\\w+\\.\\w+(,\\s*\\w+@\\w+\\.\\w+)*", message = "{email.invalid}")
    String email;

//    @Column(table = "UserToken")
    @ElementCollection
    @CollectionTable(name = "UserToken", joinColumns = @JoinColumn(name="userId"))
    @GenericGenerator(name = "tokenGen", strategy = "com.adamsimon.core.generators.TokenGenerator")
    @GeneratedValue(generator = "tokenGen")
    List<String> token;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getToken() {
        return token;
    }

    public void setToken(List<String> token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return Objects.equals(getUserId(), users.getUserId()) &&
                Objects.equals(getName(), users.getName()) &&
                Objects.equals(getEmail(), users.getEmail()) &&
                Objects.equals(getToken(), users.getToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getName(), getEmail(), getToken());
    }
}
