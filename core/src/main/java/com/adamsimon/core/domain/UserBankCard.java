package com.adamsimon.core.domain;

import com.adamsimon.commons.enums.CurrencyEnum;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "UserBankCard")
public class UserBankCard {

    @Column(name = "userId")
    Long userId;

    @Id
    @Column(name = "cardId")
    @GenericGenerator(name = "cardIdGen", strategy = "com.adamsimon.core.generators.CardIdGenerator")
    @GeneratedValue(generator = "cardId")
    @Pattern(regexp = "^C\\d{4}$", message = "{cardId.invalid}")
    String cardId;

    @Column(length = 16)
    @Pattern(regexp = "^\\d{16}$")
    String cardnumber;

    @Column(length = 3)
    @Pattern(regexp = "^\\d{3}$")
    String cvc;

    @Column(length = 100)
    String name;

    BigDecimal amount;

    String currency;

    public UserBankCard() {}

    public UserBankCard(Long userId,
                        String cardId,
                        String cardnumber,
                        String cvc,
                        String name,
                        BigDecimal amount,
                        String currencyEnum) {
        this.userId = userId;
        this.cardId = cardId;
        this.cardnumber = cardnumber;
        this.cvc = cvc;
        this.name = name;
        this.amount = amount;
        this.currency = currency;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyEnum currencyEnum) {
        this.currency = currencyEnum.toString();
    }

    @Override
    public String toString() {
        return "UserBankCard{" +
                "userId=" + userId +
                ", cardId='" + cardId + '\'' +
                ", cardnumber='" + cardnumber + '\'' +
                ", cvc='" + cvc + '\'' +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", currency=" + currency +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserBankCard that = (UserBankCard) o;
        return Objects.equals(getUserId(), that.getUserId()) &&
                Objects.equals(getCardId(), that.getCardId()) &&
                Objects.equals(getCardnumber(), that.getCardnumber()) &&
                Objects.equals(getCvc(), that.getCvc()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getAmount(), that.getAmount()) &&
                Objects.equals(getCurrency(), that.getCurrency());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getCardId(), getCardnumber(), getCvc(), getName(), getAmount(), getCurrency());
    }
}
