package com.adamsimon.commons.dto.responseDto;

import java.util.Objects;

public class ErrorResponse {
    private String text;
    private Integer code;

    public ErrorResponse() {}

    public ErrorResponse(String text, Integer code) {
        this.text = text;
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "{ \n" +
                "\"success\": " + "\"" + "false" + "\", \n" +
                "\"text\": " + "\"" + text + "\", \n" +
                "\"code\": " + "\"" + code + "\"\n" +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorResponse that = (ErrorResponse) o;
        return Objects.equals(getText(), that.getText()) &&
                Objects.equals(getCode(), that.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getText(), getCode());
    }
}
