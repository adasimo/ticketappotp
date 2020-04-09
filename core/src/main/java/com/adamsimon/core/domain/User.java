package com.adamsimon.core.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @GenericGenerator(name = "userIdGen", strategy = "com.adamsimon.core.generators.UserIdGenerator")
    @GeneratedValue(generator = "userIdGen")
    @Column(name = "userId", nullable = false)
    private Long userId;
    @NotNull(message = "{name.required}")
    @Column(name = "name", length = 100)
    private String name;
    @NotNull(message = "{email.required}")
    @Pattern(regexp = "\\w+@\\w+\\.\\w+(,\\s*\\w+@\\w+\\.\\w+)*", message = "{email.invalid}")
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "UserToken", joinColumns = @JoinColumn(name="userId"))
    @GenericGenerator(name = "tokenGen", strategy = "com.adamsimon.core.generators.TokenGenerator")
    @GeneratedValue(generator = "tokenGen")
    private List<String> token;

    public User() {}

    public User(Long userId, String name, String email, List<String> token) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.token = token;
    }

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
        User user = (User) o;
        return Objects.equals(getUserId(), user.getUserId()) &&
                Objects.equals(getName(), user.getName()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                Objects.equals(getToken(), user.getToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getName(), getEmail(), getToken());
    }
}
